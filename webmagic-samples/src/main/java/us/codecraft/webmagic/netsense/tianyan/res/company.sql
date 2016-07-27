CREATE TABLE t_ori_company_set
(
    name VARCHAR(50) PRIMARY KEY NOT NULL COMMENT '公司名称',
    id VARCHAR(50) COMMENT '公司ID,天眼查属性',
    type VARCHAR(50) COMMENT '公司类型，天眼查属性',
    sort VARCHAR(50) COMMENT '公司分类，房地产等',
    level INT(10) COMMENT '排行榜，房地产500强用的',
    area VARCHAR(50) COMMENT '企业所在地',
    updatetime TIMESTAMP,
    createtime TIMESTAMP DEFAULT 'CURRENT_TIMESTAMP'
);

############################################

CREATE TABLE t_ori_company_info
(
    name VARCHAR(50) PRIMARY KEY NOT NULL COMMENT '公司名称',
    orgCode VARCHAR(50) COMMENT '组织机构代码',
    legalPerson VARCHAR(50) COMMENT '法人代表',
    regFund VARCHAR(50) COMMENT '注册资金',
    score VARCHAR(50) COMMENT '公司评分',
    regStatus VARCHAR(50) COMMENT '注册状态',
    regDate VARCHAR(50) COMMENT '注册日期',
    industry VARCHAR(50) COMMENT '产业',
    regNum VARCHAR(50) COMMENT '工商注册号',
    type VARCHAR(50) COMMENT '企业类型',
    businessTerm VARCHAR(50) COMMENT '营业期限',
    regAuth VARCHAR(50) COMMENT '登记机关',
    approvalDate VARCHAR(50) COMMENT '核准日期',
    creditCode VARCHAR(50) COMMENT '统一信用代码',
    regAddress VARCHAR(50) COMMENT '注册地址',
    businessScope TEXT COMMENT '经营范围',
    url VARCHAR(255) COMMENT '公司详情URL',
    createtime TIMESTAMP DEFAULT 'CURRENT_TIMESTAMP',
    updatetime TIMESTAMP
);
CREATE UNIQUE INDEX t_ori_company_info_name_uindex ON t_ori_company_info (name);

############################################
CREATE TABLE t_ori_company_relation
(
    srcCompany VARCHAR(50) NOT NULL COMMENT '投资公司',
    desCompany VARCHAR(50) NOT NULL COMMENT '被投资公司',
    found VARCHAR(50) COMMENT '投资金额',
    desUrl VARCHAR(255) COMMENT '被投资公司详情URL',
    createtime TIMESTAMP DEFAULT 'CURRENT_TIMESTAMP',
    updatetime TIMESTAMP,
    url VARCHAR(255),
    CONSTRAINT `PRIMARY` PRIMARY KEY (srcCompany, desCompany)
);