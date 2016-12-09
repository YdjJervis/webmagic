package us.codecraft.webmagic.amazon.wsclient;

import com.eccang.pojo.AsinPriorityReq;
import com.eccang.pojo.AsinQueryReq;
import com.eccang.pojo.AsinReq;
import com.eccang.wsclient.asin.AsinWSService;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.ImportData;
import us.codecraft.webmagic.samples.amazon.service.ImportDataService;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description:
 * @date 2016/12/7 16:03
 */
public class AsinWS extends SpringTestCase {

    @Autowired
    private ImportDataService mImportDataService;

    @Test
    public void testChangePriority() {
        AsinPriorityReq priorityReq = new AsinPriorityReq();
        priorityReq.cutomerCode = "EC_001";
        priorityReq.platformCode = "ERP";
        priorityReq.token = "123456789";

        AsinPriorityReq.Asin asin = priorityReq.new Asin();
        asin.priority = 1;
        asin.asin = "B01M70JMIJ";

        priorityReq.data.add(asin);

        String json = new AsinWSService().getAsinWSPort().setPriority(new Gson().toJson(priorityReq));
        System.out.println(json);
    }

    @Test
    public void testQueryAsin() {
        AsinQueryReq queryReq = new AsinQueryReq();
        queryReq.cutomerCode = "EC_001";
        queryReq.platformCode = "ERP";
        queryReq.token = "123456789";

        AsinQueryReq.Asin asin = queryReq.new Asin();
        asin.asin = "B01M6WRC9R1";

        queryReq.data.add(asin);

        String json = new AsinWSService().getAsinWSPort().getAsins(new Gson().toJson(queryReq));
        System.out.println(json);
    }

    @Test
    public void testAsinBatchImport() {
        AsinReq asinReq = new AsinReq();
        asinReq.cutomerCode = "EC_001";
        asinReq.platformCode = "ERP";
        asinReq.token = "123456789";

        List<ImportData> importDataList = mImportDataService.find(null);

        for (ImportData importData : importDataList) {
            AsinReq.Asin asin = asinReq.new Asin();
            asin.asin = importData.getAsin();
            asin.siteCode = importData.getSiteCode();
            asin.priority = 0;
            asin.star = "0-0-1-1-1";
            asinReq.data.add(asin);
        }

        System.out.println(new Gson().toJson(asinReq));
        String json = new AsinWSService().getAsinWSPort().addToCrawl(new Gson().toJson(asinReq));
        System.out.println(json);

    }
}