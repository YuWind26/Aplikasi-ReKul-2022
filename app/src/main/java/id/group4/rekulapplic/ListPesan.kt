package id.group4.rekulapplic

object ListPesan {

    val pesanku = arrayOf(
    "Assalamualaikum Bapak Yusuf , Maaf mengganggu waktunya",
    "Permisi Ibu Nur, Izin bertanya Bu",
    "Assalamualaikum Bapak Yusuf , Maaf mengganggu waktunya",
    "Permisi Ibu Nur, Izin bertanya Bu",
    "Assalamualaikum Bapak Yusuf , Maaf mengganggu waktunya",
    "Permisi Ibu Nur, Izin bertanya Bu",
    "Assalamualaikum Bapak Yusuf , Maaf mengganggu waktunya",
    "Permisi Ibu Nur, Izin bertanya Bu",
    "Assalamualaikum Bapak Yusuf , Maaf mengganggu waktunya",
    "Permisi Ibu Nur, Izin bertanya Bu"
    )

    val listData: ArrayList<DataPesan>
        get() {
            val list = arrayListOf<DataPesan>()
            for (position in pesanku.indices){
                val pesan = DataPesan()
                pesan.pesan = pesanku[position]
                list.add(pesan)
            }
            return list
        }

}