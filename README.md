postgresql-treemap-analyse
==========================

D3.js treemap for postgresql table&index volume analyse.
J2EE project based on d3.js, resteasy, cdi features to deploy in WELD container.

1) edit DatabaseAnalyseServiceImpl.java fields :
private final static String DB_URL = "jdbc:postgresql://127.0.0.1:5432/sample";
private final static String DB_USER = "postgres";
private final static String DB_PASS = "postgres";

2) build with gradle cmd line : gradle install
3) deploy in wildfly or weldcontainer
4) goto url :http://127.0.0.1:8080/treemap