xof 0303txt 0032
template XSkinMeshHeader {
 <3cf169ce-ff7c-44ab-93c0-f78f62d172e2>
 WORD nMaxSkinWeightsPerVertex;
 WORD nMaxSkinWeightsPerFace;
 WORD nBones;
}

template VertexDuplicationIndices {
 <b8d65549-d7c9-4995-89cf-53a9a8b031e3>
 DWORD nIndices;
 DWORD nOriginalVertices;
 array DWORD indices[nIndices];
}

template SkinWeights {
 <6f0d123b-bad2-4167-a0d0-80224f25fabb>
 STRING transformNodeName;
 DWORD nWeights;
 array DWORD vertexIndices[nWeights];
 array FLOAT weights[nWeights];
 Matrix4x4 matrixOffset;
}

template AnimTicksPerSecond {
 <9e415a43-7ba6-4a73-8743-b73d47e88476>
 DWORD AnimTicksPerSecond;
}

template FVFData {
 <b6e70a0e-8ef9-4e83-94ad-ecc8b0c04897>
 DWORD dwFVF;
 DWORD nDWords;
 array DWORD data[nDWords];
}


AnimTicksPerSecond {
 24;
}

Frame Scene_Root {
 

 FrameTransformMatrix {
  1.000000,0.000000,0.000000,0.000000,0.000000,1.000000,0.000000,0.000000,0.000000,0.000000,1.000000,0.000000,0.000000,0.000000,0.000000,1.000000;;
 }

 Frame World {
  

  FrameTransformMatrix {
   1.000000,0.000000,0.000000,0.000000,0.000000,1.000000,0.000000,0.000000,0.000000,0.000000,1.000000,0.000000,0.000000,0.000000,0.000000,1.000000;;
  }

  Mesh World {
   72;
   234.000000;416.000000;94.000000;,
   234.000000;0.000000;-98.000000;,
   234.000000;416.000000;-98.000000;,
   234.000000;0.000000;94.000000;,
   418.000000;0.000000;94.000000;,
   418.000000;416.000000;-98.000000;,
   418.000000;0.000000;-98.000000;,
   418.000000;416.000000;94.000000;,
   234.000000;0.000000;-98.000000;,
   418.000000;0.000000;94.000000;,
   418.000000;0.000000;-98.000000;,
   234.000000;0.000000;94.000000;,
   234.000000;416.000000;94.000000;,
   418.000000;416.000000;-98.000000;,
   418.000000;416.000000;94.000000;,
   234.000000;416.000000;-98.000000;,
   418.000000;0.000000;94.000000;,
   234.000000;416.000000;94.000000;,
   418.000000;416.000000;94.000000;,
   234.000000;0.000000;94.000000;,
   234.000000;0.000000;-98.000000;,
   418.000000;416.000000;-98.000000;,
   234.000000;416.000000;-98.000000;,
   418.000000;0.000000;-98.000000;,
   -418.000000;416.000000;94.000000;,
   -418.000000;0.000000;-98.000000;,
   -418.000000;416.000000;-98.000000;,
   -418.000000;0.000000;94.000000;,
   -234.000000;0.000000;94.000000;,
   -234.000000;416.000000;-98.000000;,
   -234.000000;0.000000;-98.000000;,
   -234.000000;416.000000;94.000000;,
   -418.000000;0.000000;-98.000000;,
   -234.000000;0.000000;94.000000;,
   -234.000000;0.000000;-98.000000;,
   -418.000000;0.000000;94.000000;,
   -418.000000;416.000000;94.000000;,
   -234.000000;416.000000;-98.000000;,
   -234.000000;416.000000;94.000000;,
   -418.000000;416.000000;-98.000000;,
   -234.000000;0.000000;94.000000;,
   -418.000000;416.000000;94.000000;,
   -234.000000;416.000000;94.000000;,
   -418.000000;0.000000;94.000000;,
   -418.000000;0.000000;-98.000000;,
   -234.000000;416.000000;-98.000000;,
   -418.000000;416.000000;-98.000000;,
   -234.000000;0.000000;-98.000000;,
   -234.000000;8.000000;94.000000;,
   -234.000000;0.000000;-98.000000;,
   -234.000000;8.000000;-98.000000;,
   -234.000000;0.000000;94.000000;,
   234.000000;0.000000;94.000000;,
   234.000000;8.000000;-98.000000;,
   234.000000;0.000000;-98.000000;,
   234.000000;8.000000;94.000000;,
   -234.000000;0.000000;-98.000000;,
   234.000000;0.000000;94.000000;,
   234.000000;0.000000;-98.000000;,
   -234.000000;0.000000;94.000000;,
   -234.000000;8.000000;94.000000;,
   234.000000;8.000000;-98.000000;,
   234.000000;8.000000;94.000000;,
   -234.000000;8.000000;-98.000000;,
   234.000000;0.000000;94.000000;,
   -234.000000;8.000000;94.000000;,
   234.000000;8.000000;94.000000;,
   -234.000000;0.000000;94.000000;,
   -234.000000;0.000000;-98.000000;,
   234.000000;8.000000;-98.000000;,
   -234.000000;8.000000;-98.000000;,
   234.000000;0.000000;-98.000000;;
   36;
   3;0,2,1;,
   3;0,1,3;,
   3;4,6,5;,
   3;4,5,7;,
   3;8,10,9;,
   3;8,9,11;,
   3;12,14,13;,
   3;12,13,15;,
   3;16,18,17;,
   3;16,17,19;,
   3;20,22,21;,
   3;20,21,23;,
   3;24,26,25;,
   3;24,25,27;,
   3;28,30,29;,
   3;28,29,31;,
   3;32,34,33;,
   3;32,33,35;,
   3;36,38,37;,
   3;36,37,39;,
   3;40,42,41;,
   3;40,41,43;,
   3;44,46,45;,
   3;44,45,47;,
   3;48,50,49;,
   3;48,49,51;,
   3;52,54,53;,
   3;52,53,55;,
   3;56,58,57;,
   3;56,57,59;,
   3;60,62,61;,
   3;60,61,63;,
   3;64,66,65;,
   3;64,65,67;,
   3;68,70,69;,
   3;68,69,71;;

   MeshNormals {
    72;
    -1.000000;0.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    -1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    1.000000;0.000000;0.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;-1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;0.000000;1.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    0.000000;-1.000000;0.000000;,
    0.000000;-1.000000;0.000000;;
    36;
    3;0,2,1;,
    3;0,1,3;,
    3;4,6,5;,
    3;4,5,7;,
    3;8,10,9;,
    3;8,9,11;,
    3;12,14,13;,
    3;12,13,15;,
    3;16,18,17;,
    3;16,17,19;,
    3;20,22,21;,
    3;20,21,23;,
    3;24,26,25;,
    3;24,25,27;,
    3;28,30,29;,
    3;28,29,31;,
    3;32,34,33;,
    3;32,33,35;,
    3;36,38,37;,
    3;36,37,39;,
    3;40,42,41;,
    3;40,41,43;,
    3;44,46,45;,
    3;44,45,47;,
    3;48,50,49;,
    3;48,49,51;,
    3;52,54,53;,
    3;52,53,55;,
    3;56,58,57;,
    3;56,57,59;,
    3;60,62,61;,
    3;60,61,63;,
    3;64,66,65;,
    3;64,65,67;,
    3;68,70,69;,
    3;68,69,71;;
   }

   MeshTextureCoords {
    72;
    0.183600;-0.812500;,
    -0.191400;0.000000;,
    -0.191400;-0.812500;,
    0.183600;0.000000;,
    0.183600;0.000000;,
    -0.191400;-0.812500;,
    -0.191400;0.000000;,
    0.183600;-0.812500;,
    0.457000;0.191400;,
    0.816400;-0.183600;,
    0.816400;0.191400;,
    0.457000;-0.183600;,
    0.457000;-0.183600;,
    0.816400;0.191400;,
    0.816400;-0.183600;,
    0.457000;0.191400;,
    0.816400;0.000000;,
    0.457000;-0.812500;,
    0.816400;-0.812500;,
    0.457000;0.000000;,
    0.457000;0.000000;,
    0.816400;-0.812500;,
    0.457000;-0.812500;,
    0.816400;0.000000;,
    0.183600;-0.812500;,
    -0.191400;0.000000;,
    -0.191400;-0.812500;,
    0.183600;0.000000;,
    0.183600;0.000000;,
    -0.191400;-0.812500;,
    -0.191400;0.000000;,
    0.183600;-0.812500;,
    -0.816400;0.191400;,
    -0.457000;-0.183600;,
    -0.457000;0.191400;,
    -0.816400;-0.183600;,
    -0.816400;-0.183600;,
    -0.457000;0.191400;,
    -0.457000;-0.183600;,
    -0.816400;0.191400;,
    -0.457000;0.000000;,
    -0.816400;-0.812500;,
    -0.457000;-0.812500;,
    -0.816400;0.000000;,
    -0.816400;0.000000;,
    -0.457000;-0.812500;,
    -0.816400;-0.812500;,
    -0.457000;0.000000;,
    0.183600;-0.015600;,
    -0.191400;0.000000;,
    -0.191400;-0.015600;,
    0.183600;0.000000;,
    0.183600;0.000000;,
    -0.191400;-0.015600;,
    -0.191400;0.000000;,
    0.183600;-0.015600;,
    -0.457000;0.191400;,
    0.457000;-0.183600;,
    0.457000;0.191400;,
    -0.457000;-0.183600;,
    -0.457000;-0.183600;,
    0.457000;0.191400;,
    0.457000;-0.183600;,
    -0.457000;0.191400;,
    0.457000;0.000000;,
    -0.457000;-0.015600;,
    0.457000;-0.015600;,
    -0.457000;0.000000;,
    -0.457000;0.000000;,
    0.457000;-0.015600;,
    -0.457000;-0.015600;,
    0.457000;0.000000;;
   }

   VertexDuplicationIndices {
    72;
    72;
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8,
    9,
    10,
    11,
    12,
    13,
    14,
    15,
    16,
    17,
    18,
    19,
    20,
    21,
    22,
    23,
    24,
    25,
    26,
    27,
    28,
    29,
    30,
    31,
    32,
    33,
    34,
    35,
    36,
    37,
    38,
    39,
    40,
    41,
    42,
    43,
    44,
    45,
    46,
    47,
    48,
    49,
    50,
    51,
    52,
    53,
    54,
    55,
    56,
    57,
    58,
    59,
    60,
    61,
    62,
    63,
    64,
    65,
    66,
    67,
    68,
    69,
    70,
    71;
   }

   MeshMaterialList {
    1;
    36;
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0;

    Material no_material {
     0.000000;0.000000;0.000000;0.000000;;
     0.000000;
     0.000000;0.000000;0.000000;;
     0.000000;0.000000;0.000000;;
    }
   }

   XSkinMeshHeader {
    1;
    1;
    1;
   }

   SkinWeights {
    "World";
    72;
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8,
    9,
    10,
    11,
    12,
    13,
    14,
    15,
    16,
    17,
    18,
    19,
    20,
    21,
    22,
    23,
    24,
    25,
    26,
    27,
    28,
    29,
    30,
    31,
    32,
    33,
    34,
    35,
    36,
    37,
    38,
    39,
    40,
    41,
    42,
    43,
    44,
    45,
    46,
    47,
    48,
    49,
    50,
    51,
    52,
    53,
    54,
    55,
    56,
    57,
    58,
    59,
    60,
    61,
    62,
    63,
    64,
    65,
    66,
    67,
    68,
    69,
    70,
    71;
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000,
    1.000000;
    1.000000,0.000000,0.000000,0.000000,0.000000,1.000000,0.000000,0.000000,0.000000,0.000000,1.000000,0.000000,0.000000,0.000000,0.000000,1.000000;;
   }
  }
 }
}