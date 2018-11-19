# Resource Management Service

## Subscribing for Sub-Units Updates

A client can subscribe for unit updates by sending the following message:

``` javascript
SUBSCRIBE
id:sub-1
destination:/topic/subunits

^@
```

After subcribing the client will receive a response message containing the list of sub-units with all their details:

``` javascript
MESSAGE
subscription:sub-1
message-id:sub-resp-123
destination:/topic/subunits
content-type:text/json

[
  {
    "deletedSubUnitName": "CJ",
    "resources": [
      {
        "identificationNumber": "1234",
        "plateNumber": "CJ04ASD",
        "vehicleType": "APCA",
        "captain": "Pt. Major John Doe",
        "crew": [
          "Lt. Anne Doe",
          "Ben Doe"
        ],
        "status": "AVAILABLE_IN_GARAGE"
      },
      {
        "identificationNumber": "1235",
        "plateNumber": "CJ05ASD",
        "vehicleType": "EPA",
        "captain": "Pt. Major Jack Moe",
        "crew": [
          "Lt. Anne Moe",
          "Ben Moe"
        ],
        "status": "IN_INTERVENTION"
      }
    ]
  }
]
```


## Editing a Sub-Unit

A client can start editing a sub-unit by sending the following message:

``` javascript
MESSAGE
id:lock-req-1
destination:/topic/locksubunit

[
  {
    "name": "CJ"
  }
]
```

When a sub-unit is locked all the clients will receive the following message:
``` javascript
MESSAGE
subscription:sub-1
message-id:sub-resp-123
destination:/topic/subunits
content-type:text/json

[
  {
    "name": "CJ"
  }
]
```


A client can update a sub-unit by sending the following message:
``` javascript
MESSAGE
message-id:update-req-1
destination:/topic/updatesubunit
content-type:text/json

[
  {
    "name": "CJ",
    "resources": [
      {
        "identificationNumber": "1234",
        "plateNumber": "CJ04ASD",
        "vehicleType": "APCA",
        "captain": "Pt. Major John Doe",
        "crew": [
          "Lt. Anne Doe",
          "Ben Doe"
        ],
        "status": "AVAILABLE_IN_GARAGE"
      },
      {
        "identificationNumber": "1235",
        "plateNumber": "CJ05ASD",
        "vehicleType": "EPA",
        "captain": "Pt. Major Jack Moe",
        "crew": [
          "Lt. Anne Moe",
          "Ben Moe"
        ],
        "status": "IN_INTERVENTION"
      }
    ]
  }
]
```

All the clients that are subscribed to sub-unit topic will receive the following message:
``` javascript
MESSAGE
subscription:sub-1
message-id:sub-notification-123
destination:/topic/subunits
content-type:text/json

[
  {
    "name": "CJ",
    "resources": [
      {
        "identificationNumber": "1234",
        "plateNumber": "CJ04ASD",
        "vehicleType": "APCA",
        "captain": "Pt. Major John Doe",
        "crew": [
          "Lt. Anne Doe",
          "Ben Doe"
        ],
        "status": "AVAILABLE_IN_GARAGE"
      },
      {
        "identificationNumber": "1235",
        "plateNumber": "CJ05ASD",
        "vehicleType": "EPA",
        "captain": "Pt. Major Jack Moe",
        "crew": [
          "Lt. Anne Moe",
          "Ben Moe"
        ],
        "status": "IN_INTERVENTION"
      }
    ]
  }
]
```



