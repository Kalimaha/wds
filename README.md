WDS: Web Data Server
====================

WDS acronym stands for Web Data Server. This project is used to fetch data from different source types with  standardized inputs and outputs. 

## Datasources Management

The datasources made available by WDS are configured through the JSON file stored in:

```
/src/main/webapp/datasources/datasources.json
```

Each object in this file describes a datasource as per the example below:

```json
[
    {
        "id"        :   "MYDATASOURCE",
        "driver"    :   "PostgreSQL",
        "url"       :   "jdbc:postgresql://127.0.0.1:5432/my_datasource",
        "dbName"    :   "my_db",
        "username"  :   "my_username",
        "password"  :   "my_password"
    }
]
```

Each object has the following properties:

|Name|Description|Example|
|----|-----------|-------|
|id|Unique name for the datasource. This name will be used in the REST services to access the DB.|MYDATASOURCE|
|driver|Type of connector to be used.|<ul><li>PostgreSQL</li><li>SQLServer2000</li><li>MongoDB</li><li>OrientDB</li><ul>|
|url|JDBC string for the datasource.|jdbc:postgresql://127.0.0.1:5432/my_datasource|
|dbName|The name of the database as is in the DBMS.|my_db|
|username|Username to access the DBMS.|my_username|
|password|Password to access the DBMS.|my_password|
