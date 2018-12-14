var mongoose = require("mongoose");
var location = require("./models").location;
var User = require("./models").user;
var ServiceRequest = require("./models").serviceRequest;
var express = require("express");
var router = express.Router();

router.post("/", function(req,res){
    var request = new ServiceRequest();
    console.log(req.body);
    request.user = mongoose.Types.ObjectId(req.body.user);
    request.location = mongoose.Types.ObjectId(req.body.location);

    request.save(function(err){
        if(err){
            res.status(500).send("DB error")
        }
        res.status(200).send("Sucessfully added request")
    })
})

router.get("/", function(req,res){
    ServiceRequest.find({})
        .populate({path: "user", model : "users", populate: {path:"services", model:"services"} })
        .populate("location")
        .exec(function(err,docs){
        if(err){
            console.log(err);
            res.status(500).send("DB Error")
        }else {
            response = [];
            docs.forEach(function(request){
                var requestResponse = {};
                requestResponse.id = request.id;

                //fix the user subset
                requestResponse.user = {};
                requestResponse.user.services = [];
                request.user.services.forEach(function(elem){
                    var newElem = {};
                    newElem.id = elem.id;
                    newElem.name = elem.name;
                    requestResponse.user.services.push(newElem)
                })
                requestResponse.user.id = request.user.id;
                requestResponse.user.nfcID = request.user.nfcID;
                requestResponse.user.name = request.user.name;
                //fix the location subset
                requestResponse.location = {};
                requestResponse.location.id = request.location.id;
                requestResponse.location.name = request.location.name;
                requestResponse.location.location = request.location.location;
                response.push(requestResponse);
            })
            res.status(200).send(response);
        }
    })
})

module.exports = router;