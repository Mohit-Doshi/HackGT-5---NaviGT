var mongoose = require("mongoose");
mongoose.connect("mongodb://dbuser:dbpass1@ds035787.mlab.com:35787/hackgt2018");

var express = require("express");
var app = express();
var port = 8080;
var bodyParser = require("body-parser")

var jsonParser = bodyParser.json();
app.use(bodyParser.json());


//add routes
var services = require("./services");
app.use("/api/services",services);

var users = require("./users");
app.use("/api/users", users)

var locations = require("./locations")
app.use("/api/locations", locations);

var serviceRequests = require("./serviceRequests")
app.use("/api/serviceRequests", serviceRequests);
app.listen(port, () => console.log("Listening on port " + port));



