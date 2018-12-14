var mongoose = require("mongoose");
var Service = require("./models").service;
var express = require("express")
var router = express.Router()

router.get('/' , function(req,res){
    Service.find({},function(err,docs){
        response = [];
        docs.forEach(function(serviceRecord){
            dataElem = {};
            dataElem.id = serviceRecord.id;
            dataElem.name = serviceRecord.name;
            response.push(dataElem);
        })
        res.status(200).send(response);
    })
})
router.post('/', function(req,res){
    var newService = new Service();
    newService.name = req.body.name;
    newService.save(function(err,service){
        if(err) {
            console.log(err);
            res.status(500).send(err);
        }else {
            res.status(200).send("Successfully created service " + service.name);
        }
    })
})

module.exports = router;