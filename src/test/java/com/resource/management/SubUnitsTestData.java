/************************************************************************
 ** PROJECT:   XVP
 ** LANGUAGE:  Java, J2SE JDK 1.8
 **
 ** COPYRIGHT: FREQUENTIS AG
 **            Innovationsstrasse 1
 **            A-1100 VIENNA
 **            AUSTRIA
 **            tel +43 1 811 50-0
 **
 ** The copyright to the computer program(s) herein
 ** is the property of Frequentis AG, Austria.
 ** The program(s) shall not be used and/or copied without
 ** the written permission of Frequentis AG.
 **
 ************************************************************************/
package com.resource.management;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SubUnitsTestData
{
   static final String SUBUNIT1 =
         "{\n" + "  \"name\": \"CJ\",\n" + "  \"resources\": [\n" + "    {\n" + "      \"plateNumber\": \"CJ90CDE\",\n"
               + "      \"identificationNumber\": 8945,\n" + "      \"vehicleType\": \"APCA\",\n"
               + "      \"captain\": \"Dorsey Gutierrez\",\n" + "      \"crew\": [\n"
               + "        \"Perkins Dominguez\",\n" + "        \"Maldonado Nichols\"\n" + "      ],\n"
               + "      \"status\": \"IN_INTERVENTION\"\n" + "    },\n" + "    {\n"
               + "      \"plateNumber\": \"CJ29POR\",\n" + "      \"identificationNumber\": 3253,\n"
               + "      \"vehicleType\": \"APCA\",\n" + "      \"captain\": \"Stokes Graves\",\n"
               + "      \"crew\": [\n" + "        \"King Rivers\",\n" + "        \"Olive Koch\"\n" + "      ],\n"
               + "      \"status\": \"UNAVAILABLE\"\n" + "    },\n" + "    {\n"
               + "      \"plateNumber\": \"CJ64FWY\",\n" + "      \"identificationNumber\": 3192,\n"
               + "      \"vehicleType\": \"ATA\",\n" + "      \"captain\": \"Dionne Morales\",\n"
               + "      \"crew\": [\n" + "        \"Jordan Potts\",\n" + "        \"Lorrie Sanford\"\n" + "      ],\n"
               + "      \"status\": \"UNAVAILABLE\"\n" + "    },\n" + "    {\n"
               + "      \"plateNumber\": \"CJ25FWY\",\n" + "      \"identificationNumber\": 6791,\n"
               + "      \"vehicleType\": \"APCA\",\n" + "      \"captain\": \"Dena Avila\",\n" + "      \"crew\": [\n"
               + "        \"Benson Noble\",\n" + "        \"Maxine Huber\"\n" + "      ],\n"
               + "      \"status\": \"IN_INTERVENTION\"\n" + "    },\n" + "    {\n"
               + "      \"plateNumber\": \"CJ46CDE\",\n" + "      \"identificationNumber\": 3261,\n"
               + "      \"vehicleType\": \"ATA\",\n" + "      \"captain\": \"Mcfadden Moss\",\n" + "      \"crew\": [\n"
               + "        \"Macias Langley\",\n" + "        \"Letha Duffy\"\n" + "      ],\n"
               + "      \"status\": \"UNAVAILABLE\"\n" + "    }\n" + "  ]\n" + "}";

   private static final String SUBUNIT2 =
         "{\n" + "    \"name\": \"DEJ\",\n" + "    \"resources\": [\n" + "  {\n" + "    \"plateNumber\": \"CJ57ABC\",\n"
               + "    \"identificationNumber\": 3354,\n" + "    \"vehicleType\": \"APCA\",\n"
               + "    \"captain\": \"Douglas Shepherd\",\n" + "    \"crew\": [\n" + "      \"Kristy Welch\",\n"
               + "      \"Cole Mendez\"\n" + "    ],\n" + "    \"status\": \"AVAILABLE_IN_GARAGE\"\n" + "  },\n"
               + "  {\n" + "    \"plateNumber\": \"CJ91EFS\",\n" + "    \"identificationNumber\": 5199,\n"
               + "    \"vehicleType\": \"APCA\",\n" + "    \"captain\": \"Davis Coffey\",\n" + "    \"crew\": [\n"
               + "      \"Lorie Beach\",\n" + "      \"Pat Wyatt\"\n" + "    ],\n"
               + "    \"status\": \"AVAILABLE_IN_GARAGE\"\n" + "  },\n" + "  {\n"
               + "    \"plateNumber\": \"CJ83EFS\",\n" + "    \"identificationNumber\": 2606,\n"
               + "    \"vehicleType\": \"ATA\",\n" + "    \"captain\": \"Alisa Monroe\",\n" + "    \"crew\": [\n"
               + "      \"Barton Pena\",\n" + "      \"Hobbs Aguirre\"\n" + "    ],\n"
               + "    \"status\": \"UNAVAILABLE\"\n" + "  },\n" + "  {\n" + "    \"plateNumber\": \"CJ20TYA\",\n"
               + "    \"identificationNumber\": 5039,\n" + "    \"vehicleType\": \"EPA\",\n"
               + "    \"captain\": \"Tanisha Dale\",\n" + "    \"crew\": [\n" + "      \"Johnston Justice\",\n"
               + "      \"Underwood Sharp\"\n" + "    ],\n" + "    \"status\": \"UNAVAILABLE\"\n" + "  },\n" + "  {\n"
               + "    \"plateNumber\": \"CJ59AMB\",\n" + "    \"identificationNumber\": 9894,\n"
               + "    \"vehicleType\": \"APCA\",\n" + "    \"captain\": \"Obrien Mcdowell\",\n" + "    \"crew\": [\n"
               + "      \"Angelique Durham\",\n" + "      \"Franco Dotson\"\n" + "    ],\n"
               + "    \"status\": \"UNAVAILABLE\"\n" + "  },\n" + "  {\n" + "    \"plateNumber\": \"CJ76ABC\",\n"
               + "    \"identificationNumber\": 8478,\n" + "    \"vehicleType\": \"EPA\",\n"
               + "    \"captain\": \"Concepcion Prince\",\n" + "    \"crew\": [\n" + "      \"Janie Mercer\",\n"
               + "      \"Shepherd Head\"\n" + "    ],\n" + "    \"status\": \"IN_INTERVENTION\"\n" + "  },\n" + "  {\n"
               + "    \"plateNumber\": \"CJ68CDE\",\n" + "    \"identificationNumber\": 5573,\n"
               + "    \"vehicleType\": \"APCA\",\n" + "    \"captain\": \"Munoz Vincent\",\n" + "    \"crew\": [\n"
               + "      \"Jacobson Allen\",\n" + "      \"Cornelia Baldwin\"\n" + "    ],\n"
               + "    \"status\": \"UNAVAILABLE\"\n" + "  }\n" + "]\n" + "  }";

   private static final String SUBUNIT3 =
         "{\n" + "    \"name\": \"AGHIRES\",\n" + "    \"resources\": [\n" + "  {\n"
               + "    \"plateNumber\": \"CJ61FWY\",\n" + "    \"identificationNumber\": 6301,\n"
               + "    \"vehicleType\": \"EPA\",\n" + "    \"captain\": \"Gilbert Houston\",\n" + "    \"crew\": [\n"
               + "      \"Reeves Cox\",\n" + "      \"Naomi Medina\"\n" + "    ],\n"
               + "    \"status\": \"IN_INTERVENTION\"\n" + "  },\n" + "  {\n" + "    \"plateNumber\": \"CJ61EFS\",\n"
               + "    \"identificationNumber\": 9158,\n" + "    \"vehicleType\": \"EPA\",\n"
               + "    \"captain\": \"Harrison Watson\",\n" + "    \"crew\": [\n" + "      \"Gamble Andrews\",\n"
               + "      \"Leon Rios\"\n" + "    ],\n" + "    \"status\": \"IN_INTERVENTION\"\n" + "  },\n" + "  {\n"
               + "    \"plateNumber\": \"CJ96TYA\",\n" + "    \"identificationNumber\": 9936,\n"
               + "    \"vehicleType\": \"EPA\",\n" + "    \"captain\": \"Constance Greene\",\n" + "    \"crew\": [\n"
               + "      \"Foley Keith\",\n" + "      \"Salazar Osborne\"\n" + "    ],\n"
               + "    \"status\": \"IN_INTERVENTION\"\n" + "  },\n" + "  {\n" + "    \"plateNumber\": \"CJ22CDE\",\n"
               + "    \"identificationNumber\": 3116,\n" + "    \"vehicleType\": \"APCA\",\n"
               + "    \"captain\": \"Keisha Swanson\",\n" + "    \"crew\": [\n" + "      \"Susie Foley\",\n"
               + "      \"Sampson Mcmahon\"\n" + "    ],\n" + "    \"status\": \"AVAILABLE_IN_GARAGE\"\n" + "  },\n"
               + "  {\n" + "    \"plateNumber\": \"CJ72POR\",\n" + "    \"identificationNumber\": 1869,\n"
               + "    \"vehicleType\": \"ATA\",\n" + "    \"captain\": \"Angelique Logan\",\n" + "    \"crew\": [\n"
               + "      \"Gibbs Cleveland\",\n" + "      \"Mcintyre Brown\"\n" + "    ],\n"
               + "    \"status\": \"UNAVAILABLE\"\n" + "  },\n" + "  {\n" + "    \"plateNumber\": \"CJ24CDE\",\n"
               + "    \"identificationNumber\": 9407,\n" + "    \"vehicleType\": \"APCA\",\n"
               + "    \"captain\": \"Cleveland Guerrero\",\n" + "    \"crew\": [\n" + "      \"Rose Reeves\",\n"
               + "      \"Barbara Carr\"\n" + "    ],\n" + "    \"status\": \"UNAVAILABLE\"\n" + "  }\n" + "]\n"
               + "  }";

   private static final String SUBUNIT4 =
         "{\n" + "    \"name\": \"TURDA\",\n" + "    \"resources\": [\n" + "  {\n"
               + "    \"plateNumber\": \"CJ13POR\",\n" + "    \"identificationNumber\": 5646,\n"
               + "    \"vehicleType\": \"EPA\",\n" + "    \"captain\": \"Hammond Marks\",\n" + "    \"crew\": [\n"
               + "      \"Patrica Gray\",\n" + "      \"Kerri Ford\"\n" + "    ],\n"
               + "    \"status\": \"AVAILABLE_IN_GARAGE\"\n" + "  },\n" + "  {\n"
               + "    \"plateNumber\": \"CJ71TYA\",\n" + "    \"identificationNumber\": 2489,\n"
               + "    \"vehicleType\": \"EPA\",\n" + "    \"captain\": \"Tara Maxwell\",\n" + "    \"crew\": [\n"
               + "      \"Hines Walls\",\n" + "      \"Hilda Grant\"\n" + "    ],\n"
               + "    \"status\": \"IN_INTERVENTION\"\n" + "  },\n" + "  {\n" + "    \"plateNumber\": \"CJ85POR\",\n"
               + "    \"identificationNumber\": 4034,\n" + "    \"vehicleType\": \"APCA\",\n"
               + "    \"captain\": \"Young Finch\",\n" + "    \"crew\": [\n" + "      \"Terra Vaughn\",\n"
               + "      \"Ewing Nielsen\"\n" + "    ],\n" + "    \"status\": \"IN_INTERVENTION\"\n" + "  },\n" + "  {\n"
               + "    \"plateNumber\": \"CJ51FWY\",\n" + "    \"identificationNumber\": 5474,\n"
               + "    \"vehicleType\": \"APCA\",\n" + "    \"captain\": \"Henry Wells\",\n" + "    \"crew\": [\n"
               + "      \"Dale Bentley\",\n" + "      \"Wilcox Dotson\"\n" + "    ],\n"
               + "    \"status\": \"IN_INTERVENTION\"\n" + "  },\n" + "  {\n" + "    \"plateNumber\": \"CJ24FWY\",\n"
               + "    \"identificationNumber\": 5246,\n" + "    \"vehicleType\": \"EPA\",\n"
               + "    \"captain\": \"Gross Fowler\",\n" + "    \"crew\": [\n" + "      \"Morse Morrison\",\n"
               + "      \"Yates Donaldson\"\n" + "    ],\n" + "    \"status\": \"AVAILABLE_IN_GARAGE\"\n" + "  },\n"
               + "  {\n" + "    \"plateNumber\": \"CJ53POR\",\n" + "    \"identificationNumber\": 1603,\n"
               + "    \"vehicleType\": \"ATA\",\n" + "    \"captain\": \"Mclean Castillo\",\n" + "    \"crew\": [\n"
               + "      \"Zimmerman Cash\",\n" + "      \"Walsh Barber\"\n" + "    ],\n"
               + "    \"status\": \"UNAVAILABLE\"\n" + "  }\n" + "]\n" + "  }";

   private static final String SUBUNIT5 =
         "{\n" + "    \"name\": \"HUEDIN\",\n" + "    \"resources\": [\n" + "  {\n"
               + "    \"plateNumber\": \"CJ52AMB\",\n" + "    \"identificationNumber\": 4721,\n"
               + "    \"vehicleType\": \"ATA\",\n" + "    \"captain\": \"Debora Peck\",\n" + "    \"crew\": [\n"
               + "      \"Stafford Swanson\",\n" + "      \"Steele Donovan\"\n" + "    ],\n"
               + "    \"status\": \"UNAVAILABLE\"\n" + "  },\n" + "  {\n" + "    \"plateNumber\": \"CJ89AMB\",\n"
               + "    \"identificationNumber\": 5931,\n" + "    \"vehicleType\": \"APCA\",\n"
               + "    \"captain\": \"Doris Serrano\",\n" + "    \"crew\": [\n" + "      \"Kristie Cunningham\",\n"
               + "      \"Velasquez Bernard\"\n" + "    ],\n" + "    \"status\": \"AVAILABLE_IN_GARAGE\"\n" + "  },\n"
               + "  {\n" + "    \"plateNumber\": \"CJ27ABC\",\n" + "    \"identificationNumber\": 2207,\n"
               + "    \"vehicleType\": \"APCA\",\n" + "    \"captain\": \"Courtney Leonard\",\n" + "    \"crew\": [\n"
               + "      \"Jefferson Osborn\",\n" + "      \"Manuela Davenport\"\n" + "    ],\n"
               + "    \"status\": \"UNAVAILABLE\"\n" + "  },\n" + "  {\n" + "    \"plateNumber\": \"CJ72EFS\",\n"
               + "    \"identificationNumber\": 2047,\n" + "    \"vehicleType\": \"EPA\",\n"
               + "    \"captain\": \"Gale Gates\",\n" + "    \"crew\": [\n" + "      \"Brandi Snider\",\n"
               + "      \"Katheryn Stephens\"\n" + "    ],\n" + "    \"status\": \"AVAILABLE_IN_GARAGE\"\n" + "  },\n"
               + "  {\n" + "    \"plateNumber\": \"CJ51AMB\",\n" + "    \"identificationNumber\": 9197,\n"
               + "    \"vehicleType\": \"EPA\",\n" + "    \"captain\": \"Villarreal James\",\n" + "    \"crew\": [\n"
               + "      \"Sampson Hughes\",\n" + "      \"Cara Williams\"\n" + "    ],\n"
               + "    \"status\": \"IN_INTERVENTION\"\n" + "  }\n" + "]\n" + "  }";

   static final List<String> ALL_UNITS =
         new ArrayList<>( Arrays.asList( SUBUNIT1, SUBUNIT2, SUBUNIT3, SUBUNIT4, SUBUNIT5 ) );
}
