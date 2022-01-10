package id.group4.rekulapplic

data class User(
    var id : String ?= null,
    var matakuliah:String ?= null,
    var deskripsi:String ?= null,
    var deadline:String ?= null
){
    constructor():this("","","",""){

    }
}
