WDS: Web Data Server
====================

WDS acronym stands for Web Data Server. This project is used to fetch data from different source types with  standardized inputs and outputs. 

## Datasources Management

The datasources made available by WDS are configured through JSON files contained in the /src/main/webapp/datasources folder. Each file in this directory is structured as a JSON array of objects as per the example below:

```json
[
    {
        "id": "MYDATASOURCE",
        "driver": "PostgreSQL",
        "url": "jdbc:postgresql://127.0.0.1:5432/my_datasource",
        "dbName": "my_db",
        "username": "my_username",
        "password": "my_password"
    }
]
```

Each object in the array must contain the following elements:

|Name|Description|Example|
|----|-----------|-------|
|id|Unique name for the datasource. This name will be used in the REST services to access the DB.|MYDATASOURCE|
|driver|Type of connector to be used.|<ul><li>PostgreSQL</li><li>SQLServer2000</li><li>MongoDB</li><li>OrientDB</li><ul>|
