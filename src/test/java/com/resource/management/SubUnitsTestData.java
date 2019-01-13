package com.resource.management;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SubUnitsTestData {
    static final String SUBUNIT1 =
            "{\n" +
                    "  \"_id\": \"RES\",\n" +
                    "  \"lastUpdate\": \"2018-11-20T10:49:48Z\",\n" +
                    "  \"lockedResourceTypeBySessionId\": null,\n" +
                    "  \"resources\": [\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ90DDD\",\n" +
                    "      \"identificationNumber\": \"21312\",\n" +
                    "      \"vehicleType\": \"APCA\",\n" +
                    "      \"captain\": \"Dorsey Gutierrez\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Perkins Dominguez\",\n" +
                    "        \"Maldonado Nichols\"\n" +
                    "      ],\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"IN_MISSION\",\n" +
                    "            \"key\": \"Incediu 1\",\n" +
                    "            \"description\": \"Incendiu mare\",\n" +
                    "            \"crew\": [\n" +
                    "              \"Perkins Dominguez\",\n" +
                    "              \"Maldonado Nichols\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"UNAVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

    static final String SUBUNIT1_UPDATED =
            "{\n" +
                    "  \"_id\": \"RES\",\n" +
                    "  \"lastUpdate\": \"2018-11-20T10:49:48Z\",\n" +
                    "  \"lockedResourceTypeBySessionId\": null,\n" +
                    "  \"resources\": [\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ90DDD\",\n" +
                    "      \"identificationNumber\": \"21312\",\n" +
                    "      \"vehicleType\": \"APCA\",\n" +
                    "      \"captain\": \"Dorsey Gutierrez\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Perkins Dominguez\",\n" +
                    "        \"Maldonado Nichols\"\n" +
                    "      ],\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"NOT_AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"IN_MISSION\",\n" +
                    "            \"key\": \"Incediu 1\",\n" +
                    "            \"description\": \"Incendiu mare\",\n" +
                    "            \"crew\": [\n" +
                    "              \"Perkins Dominguez\",\n" +
                    "              \"Maldonado Nichols\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"UNAVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

    protected static final String SUBUNIT2 =
            "{\n" +
                    "  \"_id\": \"DET 1 CJN\",\n" +
                    "  \"lastUpdate\": \"2018-11-20T10:49:48Z\",\n" +
                    "  \"lockedResourceTypeBySessionId\": null,\n" +
                    "  \"resources\": [\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ90ABC\",\n" +
                    "      \"identificationNumber\": \"8945\",\n" +
                    "      \"vehicleType\": \"APCA\",\n" +
                    "      \"captain\": \"Dorsey Gutierrez\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Perkins Dominguez\",\n" +
                    "        \"Maldonado Nichols\"\n" +
                    "      ],\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"IN_MISSION\",\n" +
                    "        \"key\": \"Incediu 1\",\n" +
                    "        \"description\": \"Incendiu mare\",\n" +
                    "        \"crew\": [\n" +
                    "          \"Perkins Dominguez\",\n" +
                    "          \"Maldonado Nichols\"\n" +
                    "        ]\n" +
                    "      },\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"IN_MISSION\",\n" +
                    "            \"key\": \"Incediu 1\",\n" +
                    "            \"description\": \"Incendiu mare\",\n" +
                    "            \"crew\": [\n" +
                    "              \"Perkins Dominguez\",\n" +
                    "              \"Maldonado Nichols\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"UNAVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ29ABD\",\n" +
                    "      \"identificationNumber\": \"3253\",\n" +
                    "      \"vehicleType\": \"SCAF\",\n" +
                    "      \"captain\": \"Stokes Graves\",\n" +
                    "      \"crew\": [\n" +
                    "        \"King Rivers\",\n" +
                    "        \"Olive Koch\"\n" +
                    "      ],\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"IN_MISSION\",\n" +
                    "            \"key\": \"Incediu 1\",\n" +
                    "            \"description\": \"Incendiu mare\",\n" +
                    "            \"crew\": [\n" +
                    "              \"King Rivers\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ABE\",\n" +
                    "      \"identificationNumber\": \"3192\",\n" +
                    "      \"vehicleType\": \"BM\",\n" +
                    "      \"captain\": \"Dionne Morales\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Jordan Potts\",\n" +
                    "        \"Lorrie Sanford\"\n" +
                    "      ],\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"IN_MISSION\",\n" +
                    "            \"key\": \"Incediu 1\",\n" +
                    "            \"description\": \"Incendiu mare\",\n" +
                    "            \"crew\": [\n" +
                    "              \"Jordan Potts\",\n" +
                    "              \"Lorrie Sanford\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ABF\",\n" +
                    "      \"identificationNumber\": \"6791\",\n" +
                    "      \"vehicleType\": \"PIRO\",\n" +
                    "      \"captain\": \"Dena Avila\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Benson Noble\",\n" +
                    "        \"Maxine Huber\"\n" +
                    "      ],\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"IN_MISSION\",\n" +
                    "            \"key\": \"Incediu 1\",\n" +
                    "            \"description\": \"Incendiu mare\",\n" +
                    "            \"crew\": [\n" +
                    "              \"Benson Noble\",\n" +
                    "              \"Maxine Huber\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ABG\",\n" +
                    "      \"identificationNumber\": \"3261\",\n" +
                    "      \"vehicleType\": \"EPA\",\n" +
                    "      \"captain\": \"Mcfadden Moss\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Macias Langley\",\n" +
                    "        \"Letha Duffy\"\n" +
                    "      ],\n" +
                    "      \"type\": \"OTHER\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"IN_MISSION\",\n" +
                    "            \"key\": \"Incediu 1\",\n" +
                    "            \"description\": \"Incendiu mare\",\n" +
                    "            \"crew\": [\n" +
                    "              \"Macias Langley\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ABH\",\n" +
                    "      \"identificationNumber\": \"3263\",\n" +
                    "      \"vehicleType\": \"EPA\",\n" +
                    "      \"captain\": \"Mcfadden Moss\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Macias Langley\",\n" +
                    "        \"Letha Duffy\"\n" +
                    "      ],\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

    private static final String SUBUNIT3 =
            "{\n" +
                    "  \"_id\": \"DET 2 CJN\",\n" +
                    "  \"lastUpdate\": \"2018-11-20T10:49:48Z\",\n" +
                    "  \"lockedResourceTypeBySessionId\": null,\n" +
                    "  \"resources\": [\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ABQ\",\n" +
                    "      \"identificationNumber\": \"4721\",\n" +
                    "      \"vehicleType\": \"APCA\",\n" +
                    "      \"captain\": \"Debora Peck\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Stafford Swanson\",\n" +
                    "        \"Steele Donovan\"\n" +
                    "      ],\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ABR\",\n" +
                    "      \"identificationNumber\": \"5931\",\n" +
                    "      \"vehicleType\": \"SCAF\",\n" +
                    "      \"captain\": \"Doris Serrano\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Kristie Cunningham\",\n" +
                    "        \"Velasquez Bernard\"\n" +
                    "      ],\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ABS\",\n" +
                    "      \"identificationNumber\": \"2207\",\n" +
                    "      \"vehicleType\": \"EPA\",\n" +
                    "      \"captain\": \"Courtney Leonard\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Jefferson Osborn\",\n" +
                    "        \"Manuela Davenport\"\n" +
                    "      ],\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ABT\",\n" +
                    "      \"identificationNumber\": \"2047\",\n" +
                    "      \"vehicleType\": \"EPA\",\n" +
                    "      \"captain\": \"Gale Gates\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Brandi Snider\",\n" +
                    "        \"Katheryn Stephens\"\n" +
                    "      ],\n" +
                    "      \"type\": \"OTHER\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ABU\",\n" +
                    "      \"identificationNumber\": \"9197\",\n" +
                    "      \"vehicleType\": \"MU\",\n" +
                    "      \"captain\": \"Villarreal James\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Sampson Hughes\",\n" +
                    "        \"Cara Williams\"\n" +
                    "      ],\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}\n";

    private static final String SUBUNIT4 =
            "{\n" +
                    "  \"_id\": \"G2 GIL\",\n" +
                    "  \"lastUpdate\": \"2018-11-20T10:49:48Z\",\n" +
                    "  \"lockedResourceTypeBySessionId\": null,\n" +
                    "  \"resources\": [\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ACA\",\n" +
                    "      \"identificationNumber\": \"4721\",\n" +
                    "      \"vehicleType\": \"APCA\",\n" +
                    "      \"captain\": \"Debora Peck\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Stafford Swanson\",\n" +
                    "        \"Steele Donovan\"\n" +
                    "      ],\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"UNAVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"UNAVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"IN_MISSION\",\n" +
                    "            \"key\": \"Incediu 1\",\n" +
                    "            \"description\": \"Incendiu mare\",\n" +
                    "            \"crew\": [\n" +
                    "              \"Stafford Swanson\",\n" +
                    "              \"Steele Donovan\",\n" +
                    "              \"Perkins Dominguez\",\n" +
                    "              \"Maldonado Nichols\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64ADA\",\n" +
                    "      \"identificationNumber\": \"5931\",\n" +
                    "      \"vehicleType\": \"EPA\",\n" +
                    "      \"captain\": \"Doris Serrano\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Kristie Cunningham\",\n" +
                    "        \"Velasquez Bernard\"\n" +
                    "      ],\n" +
                    "      \"type\": \"FIRST_INTERVENTION\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"UNAVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"IN_MISSION\",\n" +
                    "            \"key\": \"Incediu 3\",\n" +
                    "            \"description\": \"Incendiu micut\",\n" +
                    "            \"crew\": [\n" +
                    "              \"Kristie Cunningham\",\n" +
                    "              \"Velasquez Bernard\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}\n";
    private static final String SUBUNIT5 =
            "{\n" +
                    "  \"_id\": \"PL FLO\",\n" +
                    "  \"lastUpdate\": \"2018-11-20T10:49:48Z\",\n" +
                    "  \"lockedResourceTypeBySessionId\": null,\n" +
                    "  \"resources\": [\n" +
                    "    {\n" +
                    "      \"_id\": \"CJ64AGA\",\n" +
                    "      \"identificationNumber\": \"5931\",\n" +
                    "      \"vehicleType\": \"EPA\",\n" +
                    "      \"captain\": \"Doris Serrano\",\n" +
                    "      \"crew\": [\n" +
                    "        \"Kristie Cunningham\",\n" +
                    "        \"Velasquez Bernard\"\n" +
                    "      ],\n" +
                    "      \"type\": \"OTHER\",\n" +
                    "      \"status\": {\n" +
                    "        \"status\": \"AVAILABLE\"\n" +
                    "      },\n" +
                    "      \"resourceLogs\": [\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:48Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"UNAVAILABLE\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:33Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"IN_MISSION\",\n" +
                    "            \"key\": \"Incediu 3\",\n" +
                    "            \"description\": \"Incendiu mic\",\n" +
                    "            \"crew\": [\n" +
                    "              \"Kristie Cunningham\",\n" +
                    "              \"Velasquez Bernard\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"changedAt\": \"2018-11-20T10:49:39Z\",\n" +
                    "          \"changedBy\": \"10.17.23.32\",\n" +
                    "          \"status\": {\n" +
                    "            \"status\": \"AVAILABLE\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}\n";

    static final List<String> ALL_UNITS =
            new ArrayList<>(Arrays.asList(SUBUNIT1, SUBUNIT2, SUBUNIT3, SUBUNIT4, SUBUNIT5));
}
