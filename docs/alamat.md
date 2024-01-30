# Alamat API Spec

## Create Alamat

Endpoint : POST /api/biodatas/{idBiodata}/alamats

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "alamat" : "jln.seoul",
  "rtrw" : "001001",
  "kelDesa" : "seoul",
  "kecamatan" : "daegu",
}
```

Response Body (Success) :

```json
{
  "data" : {
    "id" : "randomstring",
    "alamat" : "jln.seoul",
    "rtrw" : "001001",
    "kelDesa" : "seoul",
    "kecamatan" : "daegu",
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Biodata is not found"
}
```

## Update Alamat

Endpoint : PUT /api/biodatas/{idBiodata}/alamats/{idAlamat}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "alamat" : "jln.korut",
  "rtrw" : "001001",
  "kelDesa" : "desakim",
  "kecamatan" : "kecjongun"
}
```

Response Body (Success) :

```json
{
  "data" : {
    "id" : "randomstring",
    "alamat" : "jln.korut",
    "rtrw" : "001001",
    "kelDesa" : "desakim",
    "kecamatan" : "kecjongun"
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Alamat is not found"
}
```

## Get Alamat

Endpoint : GET /api/biodatas/{idBiodata}/alamats/{idAlamat}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : {
    "id" : "randomstring",
    "alamat" : "jln.korut",
    "rtrw" : "001001",
    "kelDesa" : "desakim",
    "kecamatan" : "kecjongun",
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Alamat is not found"
}
```

## Remove Alamat

Endpoint : DELETE /api/biodatas/{idBiodata}/alamats/{idAlamat}

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
  "errors" : "Alamat is not found"
}
```

## List Alamat

Endpoint : GET /api/biodatas/{idBiodata}/alamats

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data": [
    {
      "id": "randomstring",
      "alamat" : "jln.korut",
      "rtrw" : "001001",
      "kelDesa" : "desakim",
      "kecamatan" : "kecjongun",
    }
  ]
}
```

Response Body (Failed) :

```json
{
  "errors" : "Biodata is not found"
}
```