package ru.mipt.npm.muon.monitor

actual fun readResource(path: String): String = js("require(path)")

// TODO replace by resource
internal actual fun readMonitorConfig(): String {
    return """
        --Place-|-SC16-|-TB-CHN-|-HB-CHN-|-X-coord-|-Y-coord-|-Z-coord-|-Theta-|-Phi
        ----------------------------------------------------------------------------
         RT100     SC86     3        0         0      1000         0     0      270
         RT100     SC87     6        1         0       500         0     0      270
         RT100     SC88     8        2         0         0         0     0      270
         RT100     SC91     9        3       500      1000         0     0      270
         RT100     SC92    10        4       500       500         0     0      270
         RT100     SC93    11        5       500         0         0     0      270
         RT100     SC94    12        6      1000      1000         0     0      270
         RT100     SC85    13        7      1000       500         0     0      270
         RT100     SC96    15        8      1000         0         0     0      270
        ###
         RT100     SC81    26       12       250       750       180     0      270
         RT100     SC82    27       11       250       250       180     0      270
         RT100     SC83    28       23       750       750       180     0      270
         RT100     SC84    29        9       750       250       180     0      270
        ###
         RT100     SC72    80       21      1000         0       346     0      270
         RT100     SC73    79       20      1000       500       346     0      270
         RT100     SC74    78       19      1000      1000       346     0      270
         RT100     SC75    77       18       500         0       346     0      270
         RT100     SC76    84       17       500       500       346     0      270
         RT100     SC77    75       16       500      1000       346     0      270
         RT100     SC78    74       15         0         0       346     0      270
         RT100     SC79    73       14         0       500       346     0      270
         RT100     SC80    72       13         0      1000       346     0      270
        STOP
    """.trimIndent()
}