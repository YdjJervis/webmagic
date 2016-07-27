package us.codecraft.webmagic.netsense.dao.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ParameterizedRowMapperFactory {

	private static Log log = LogFactory.getLog(ParameterizedRowMapperFactory.class);
	@SuppressWarnings("rawtypes")
	private static final Map<Class, ParameterizedRowMapper> map = new HashMap<Class, ParameterizedRowMapper>();

	@SuppressWarnings("unchecked")
	public static <T> ParameterizedRowMapper<T> getParameterizedRowMapper(Class<T> clazz) {
		if (map.containsKey(clazz)) {
			return map.get(clazz);
		}
		Map<String, Method[]> methodMap = new HashMap<String, Method[]>();
		Class<ResultSet> rsclazz = ResultSet.class;
		for (Method method : clazz.getMethods()) {
			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				String name = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
				Class<?> parameterType = method.getParameterTypes()[0];
				try {
					if (parameterType.equals(String.class)) {
						methodMap.put(name, new Method[] { method, rsclazz.getMethod("getString", String.class) });
					} else if (parameterType.equals(int.class) || parameterType.equals(Integer.class)) {
						methodMap.put(name, new Method[] { method, rsclazz.getMethod("getInt", String.class) });
					} else if (parameterType.equals(long.class) || parameterType.equals(Long.class)) {
						methodMap.put(name, new Method[] { method, rsclazz.getMethod("getLong", String.class) });
					} else if (parameterType.equals(float.class) || parameterType.equals(Float.class)) {
						methodMap.put(name, new Method[] { method, rsclazz.getMethod("getFloat", String.class) });
					} else if (parameterType.equals(double.class) || parameterType.equals(Double.class)) {
						methodMap.put(name, new Method[] { method, rsclazz.getMethod("getDouble", String.class) });
					} else if (parameterType.equals(Date.class)) {
						methodMap.put(name, new Method[] { method, rsclazz.getMethod("getTimestamp", String.class) });
					} else {
						methodMap.put(name, new Method[] { method, rsclazz.getMethod("getString", String.class) });
					}
				} catch (Exception e) {
					log.error(e, e);
				}
			}
		}
		ParameterizedRowMapper<T> pt = createRowMapper(clazz, methodMap);
		map.put(clazz, pt);
		return pt;
	}

	private static <T> ParameterizedRowMapper<T> createRowMapper(final Class<T> clazz, final Map<String, Method[]> methodMap) {
		ParameterizedRowMapper<T> rowMapper = new ParameterizedRowMapper<T>() {
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				T resource;
				try {
					resource = clazz.newInstance();
				} catch (Exception e) {
					log.error("can't instance " + clazz.getName(), e);
					return null;
				}
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					String colName = metaData.getColumnLabel(i);
					Method[] methods = methodMap.get(colName);
					if (null != methods) {
						Method method = methods[0];
						try {
							Object data = methods[1].invoke(rs, colName);
							if (null != data) {
								method.invoke(resource, data);
							}
						} catch (Exception e) {
							log.error(method.getName() + " method error");
						}
					} else {
//						log.error("not find attr : \t" + colName);
					}
				}
				return resource;
			}
		};
		return rowMapper;
	}

}
