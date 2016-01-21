postgresql-treemap-analyse
==========================

D3.js treemap for postgresql table&index volume analyse.<br />
J2EE project based on d3.js, resteasy, cdi features to deploy in WELD container.<br />

1) edit DatabaseAnalyseServiceImpl.java fields :<br />
private final static String DB_URL = "jdbc:postgresql://127.0.0.1:5432/sample";<br />
private final static String DB_USER = "postgres";<br />
private final static String DB_PASS = "postgres";<br />

2) build with gradle cmd line : gradle install<br />
3) deploy in wildfly or weldcontainer<br />
4) goto url :http://127.0.0.1:8080/treemap<br />