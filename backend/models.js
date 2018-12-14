var mongoose = require("mongoose")
var Schema = mongoose.Schema;


var locationSchema = new mongoose.Schema({
    name: String,
    location : String
});

var serviceSchema = new mongoose.Schema({
    name : String
})

var usersSchema = new mongoose.Schema({
    name: String,
    nfcID: String,
    services : [{type:Schema.Types.ObjectId, ref:"services"}]
})

var serviceRequestSchema = new mongoose.Schema({
    location : {type:Schema.Types.ObjectId, ref:"locations"},
    user : {type:Schema.Types.ObjectId, ref:"services"}
})

module.exports.location = mongoose.model('locations', locationSchema);
module.exports.service = mongoose.model('services' , serviceSchema);
module.exports.user = mongoose.model('users', usersSchema);
module.exports.serviceRequest = mongoose.model('serviceRequest' , serviceRequestSchema);