var mongoose = require("mongoose");
var Location = require("./models").location;
var express = require("express")
var router = express.Router()

router.get("/", function(req,res){
    Location.find({}, function(err,docs){
        if(err) {
            res.status(500).send("DB error")
        }
        response = [];
        docs.forEach(function(elem){
            var locationElem = {};
            locationElem.id = elem.id;
            locationElem.name = elem.name;
            locationElem.location = elem.location;
            response.push(locationElem);
        })
        res.status(200).send(response);
    })
})

router.post("/", function(req,res){
    var newLocation = new Location();
    newLocation.name = req.body.name;
    newLocation.location = req.body.location;
    newLocation.save(function(err){
        if(err){
            res.status(500).send("DB error")
        }
        res.status(200).send("Sucessfully added new location")
    })
})

module.exports = router;