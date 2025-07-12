package com.example.fibra_labeling.data.migration.fibrafil

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

val roomCallbackZpl = object : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        db.execSQL("""
                INSERT INTO zpl_labels (code_name, name, description, zpl_file, selected) 
                VALUES (
                    'FIBRAPRINT_DEFAULT',
                    'Etiqueta Fibraprint',
                    'Template por defecto para etiquetas de productos Fibraprint',
                    'CT~~CD,~CC^~CT~^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR6,6~SD20^JUS^LRN^CI0^XZ
^XA
^MMT
^PW748
^LL0527
^LS0
^FO30,0^GFA,02560,02560,00020,:Z64:eJztlUGL00AUx2cmKYmhTJND2YNxI3sqPYjegqLNQr1vYXsT3LuXLoh4UDpuUZc9eBA/QI9lPkHJKb24R1cQ9Gb8AtKDwh7Sjm8SFzIzvQkLCz4KSf/8ePPe+/MmCP2PWgwTQyKc6xJ+LESmi5zzqSY5QohC0zzg9IQRcELTuilPOVO1seSWxrH6wY7AEL6iEaMJ4Nam5m3gIr0HiLtzUxuvhjKSutblRoFYVKE0wv+dO69pJK1iVtMc8VuWN653Q6pMnspl5UPlWPnYwOFNXH2KWwL5EArnpcg9OTkmqZJPGrzUzpUTnCr5wN6SW6mcnIrinlPIyeRqvhmaGf0WUJqvukzmaL6Bk1aoHN+ZwqNb56zSMixUTg6a8DqHZROmv1PP9Pcs0rk0ZR74W+dgepnk8rrW5UzmYyrHJJdpXFfnotIQdTG90hB1SbZKbqVo0pCUq0vSKCT3S9HADmN/8Qtp3JnKdZBeHkIhNu8NOWpjhx3zHpIrMt3AJWY+A0M4MKQrFkO7nbQTG+839m3bbu9JTdBW3sspWlsFpbQn7xMswqjoLSMs3ooI4htoFqNuHMfX8WRCaYtaTHJZGIbPRxFenDo0+mhlFUfj+PY9NnkD3LuKg3zL+8vDxdfT79GNT1hq1rrZmvfyO6/E5Jw2f1bcF8j3EjjhPKLhs4t8rhsfPFkwQmnzVsUx4OLR+HPmRNvhuMrHaAj15T+YBdyDi/qiIh7lh5nj0HC7zEcY9AvcASOENv/2gfx24gfJAGF4hb+XFLgxHPSf9nf7e4HdfogH8uNF4OfuINc9gle3S0ouCD70h6+H7SCwgwEOSq6DjtBNNnXfM4audcix5Gx/tw/OokFg+43A3rusRq5a/AEzTEgX:8162
^FT165,73^A0N,56,55^FH\^FDFIBRAPRINT^FS
^FT56,176^A0N,23,24^FH\^FDProveedor^FS
^FT56,135^A0N,23,24^FH\^FDCodigo^FS
^FT56,260^A0N,23,24^FH\^FDAlmacen^FS
^FT56,219^A0N,23,24^FH\^FDProducto^FS
^FT56,340^A0N,23,24^FH\^FDMetros Lineal^FS
^FT320,337^A0N,23,24^FH\^FDPeso Neto(Kg)^FS
^FT56,298^A0N,23,24^FH\^FDUbicacien^FS
^FT470,128^A0N,23,24^FH\^FDFecha ^FS
^FT194,218^A0N,28,28^FH\^FD:^FS
^FT194,257^A0N,28,28^FH\^FD:^FS
^FT194,136^A0N,28,28^FH\^FD:^FS
^FT194,173^A0N,28,28^FH\^FD:^FS
^FT194,342^A0N,28,28^FH\^FD:^FS
^FT194,299^A0N,28,28^FH\^FD:^FS
^FT449,336^A0N,28,28^FH\^FD:^FS
^FT570,129^A0N,28,28^FH\^FD:^FS
^FT214,176^A0N,23,24^FH\^FD{Proveedor}^FS
^FT214,135^A0N,23,24^FH\^FD{ItemCode}^FS
^FT214,260^A0N,23,24^FH\^FD{Almacen}^FS
^FB330,2,0,L,0^FT214,219^A0N,20,19^FH\^FD{name}^FS
^FT214,340^A0N,23,24^FH\^FD{MetroLineal}^FS
^FT462,337^A0N,23,24^FH\^FD{Peso}^FS
^FT214,298^A0N,23,24^FH\^FD{Ubicacion}^FS
^FT585,128^A0N,23,24^FH\^FD{CreateDate}^FS
^BY2,2,70^FT66,425^BCN,,Y,N^FD>:{CodeBar}^FS
^FT470,58^A0N,25,24^FH\^FDLote^FS
^FT520,58^A0N,25,24^FH\^FD:^FS
^FT535,58^A0N,25,24^FH\^FD{Lote}^FS
^PQ1,0,1,Y^XZ',
                    1
                )
            """.trimIndent())

        // inser into

    }
}