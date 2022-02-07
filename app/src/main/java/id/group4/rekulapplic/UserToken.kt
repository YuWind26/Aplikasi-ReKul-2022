package id.group4.rekulapplic

data class UserToken(
    var token:String ?= null,
){
    constructor():this(""){

    }
}