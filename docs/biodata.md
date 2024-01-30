# Biodata API Spec

## Create Biodata

Endpoint : POST /api/biodatas

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "nik" : "1122334455667788",
  "firstName" : "Sholeh",
  "middleName" : "Al",
  "lastName" : "Farys",
  "tempatLahir" : "South Korean",
  "tanggalLahir" : "01-01-98"
}
```

Response Body (Success) : 

```json
{
  "data": {
    "id" : "random-string",
    "nik" : "1122334455667788",
    "firstName" : "Sholeh",
    "middleName" : "Al",
    "lastName" : "Farys",
    "tempatLahir" : "South Korean",
    "tanggalLahir" : "01-01-1001"
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Email format invalid, phone format invalid, ..."
}
```

## Update Biodata

Endpoint : PUT /api/biodatas/{idBiodata}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{ //menambahkan id didalam req body -> pencarian by id
  "nik" : "1122334455667788",
  "firstName" : "Sholeh",
  "middleName" : "Al",
  "lastName" : "Farys",
  "tempatLahir" : "South Korean",
  "tanggalLahir" : "01-01-1001"
}
```

Response Body (Success) :

```json
{
  "data": {
    "id" : "random-string",
    "nik" : "1122334455667788",
    "firstName" : "Sholeh",
    "middleName" : "Al",
    "lastName" : "Farys",
    "tempatLahir" : "South Korean",
    "tanggalLahir" : "01-01-1001"
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Email format invalid, phone formar invalid, ..."
}
```

## Get Biodata

Endpoint : GET /api/biodatas/{idBiodata}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data": {
    "id" : "random-string",
    "nik" : "1122334455667788",
    "firstName" : "Sholeh",
    "middleName" : "Al",
    "lastName" : "Farys",
    "tempatLahir" : "South Korean",
    "tanggalLahir" : "01-01-1001"
  }
}
```

Response Body (Failed, 404) :

```json
{
  "errors" : "Biodata is not found"
}
```

## Search Biodata

Endpoint : GET /api/biodatas

Query Param :

- NIK : String
- name : String, Biodata first name or middle name or last name, using like query, optional
- page : Integer, start from 0, default 0
- size : Integer, default 10

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data": [
    {
      "id": "random-string",
      "nik" : "1122334455667788",
      "firstName" : "Sholeh",
      "middleName" : "Al",
      "lastName" : "Farys",
      "tempatLahir" : "South Korean",
      "tanggalLahir" : "01-01-1001"
    }
  ],
  "paging" : {
    "currentPage" : 0,
    "totalPage" : 10,
    "size" : 10
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Unauthorized"
}
```

## Remove Biodata

Endpoint : DELETE /api/biodatas/{idBiodata}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : "OK"
}
```

Response Body (Failed) :

```json
{
  "errors" : "Biodata is not found"
}
```