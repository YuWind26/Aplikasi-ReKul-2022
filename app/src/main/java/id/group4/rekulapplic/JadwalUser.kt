package id.group4.rekulapplic

data class JadwalUser(
    var id : String ?= null,
    var matakuliah:String ?= null,
    var dosen:String ?= null,
    var topik:String ?= null,
    var waktu:String ?= null
){
    constructor():this("","","","",""){

    }
}