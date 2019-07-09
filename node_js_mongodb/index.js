// Import package
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var express = require('express');
var bodyParser = require('body-parser');
var fs = require('fs-extra');
var multer = require('multer');
var path = require('path');
const router = express.Router();
const fileType = require('file-type')

var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

var MongoClient = mongodb.MongoClient;
var url = 'mongodb://localhost:27017'

const upload = multer({
    // dest:'images/', 
    limits: {fileSize: 10000000, files: 1},
    fileFilter:  (req, file, callback) => {
        if (!file.originalname.match(/\.(jpg|jpeg)$/)) {
            return callback(new Error('Only Images are allowed !'), false)
        }
        callback(null, true);
    },
    storage: multer.diskStorage({
    	filename: function (req, file, cb) {
    		cb(null, file.originalname);
    	},
    	destination: function (req, file, cb) {
    		cb(null, 'images/');
    	}
    })
}).single('image')

MongoClient.connect(url, function(err, client) {
	if(err)
		console.log('unable to connect to the mongoDB server.Error', err);
	else {

		// Upload Contacts
		app.post('/uploadC', (request, response, next) => {
			var post_data = request.body;
			var user_id = post_data.user_id;
			var contact_id = post_data.contact_id;
			var name = post_data.name;
			var phone_number = post_data.phone_number;
			var email = post_data.email;
			var photo = post_data.photo;

			var insertJson = {
				'user_id': user_id,
				'contact_id': contact_id,
				'name': name,
				'phone_number': phone_number,
				'email': email,
				'photo': photo
			};
			var db = client.db('newnodejs');

			var sameJson = {
				'user_id': user_id,
				$and: [
					{ 'contact_id': contact_id },
					{ 'contact_id': { $ne: '' } } 
					]
			};

			db.collection('contacts').find(sameJson).count(function(er, number) {
				if(number =! 0)
				{
					db.collection('contacts').deleteOne(sameJson)
					db.collection('contacts').insertOne(insertJson, function(error,res) {
						response.json('Updated a contact successfully');
						console.log('Updated a contact successfully');
					})
				}
				else 
				{
					db.collection('contacts').insertOne(insertJson, function(error,res) {
						response.json('Uploaded a contact successfully');
						console.log('Uploaded a contact successfully');
					})
				}
				
			});
			
		});

		app.post('/downloadC', (request, response, next) => {
			var post_data = request.body;
			var user_id = post_data.user_id;
			var db = client.db('newnodejs');
			db.collection('contacts').find({ 'user_id': user_id }).count(function(er, number) {
				if(number == 0)
				{
					response.json('No data to download!');
					console.log('No data to download!');
				}
				else
				{
					db.collection('contacts').find({ 'user_id': user_id }).sort({ 'name': 1 }).toArray(function(err, result) {
						if (err) throw err;
						response.json(result)
						console.log('Downloaded contacts successfully');
					})
				}
			})
		});

		app.post('/deleteC', (request, response, next) => {
			var post_data = request.body;
			var user_id = post_data.user_id;
			var contact_id = post_data.contact_id;
			var name = post_data.name;
			var phone_number = post_data.phone_number;
			var email = post_data.email;
			// var photo = post_data.photo;

			var deleteJson = {
				'user_id': user_id,
				'contact_id': contact_id,
				'name': name,
				'phone_number': phone_number,
				'email': email
			};
			var db = client.db('newnodejs');

			db.collection('contacts').find(deleteJson).count(function(er, number) {
				if(number == 0)
				{
					response.json('No data to delete!');
					console.log('No data to delete!');
				}
				else
				{
					db.collection('contacts').deleteOne(deleteJson, function(error,res) {
						response.json('Deleted a contact successfully');
						console.log('Deleted a contact successfully');
					})
				}
			})
		});

		app.post('/gallery/upload', (req, res) => {
		    upload(req, res, function (err) {
		        if (err) {
		            res.status(400).json({message: err.message})
		        } else {
		            let path = `/images/${req.file.filename}`
		            res.status(200).json({message: 'Image Uploaded Successfully !', path: path})
		        }
		    });
		});

		app.get('/images/:imagename', (req, res) => {

		    let imagename = req.params.imagename
		    let imagepath = __dirname + "/images/" + imagename
		    let image = fs.readFileSync(imagepath)
		    let mime = fileType(image).mime

		    res.writeHead(200, {'Content-Type': mime })
		    res.end(image, 'binary')
		});

		app.post('/uploadG', (request, response, next) => {
			var post_data = request.body;
			var user_id = post_data.user_id;
			var file_name = post_data.file_name;
			console.log('A picture upload request arrived');

			var insertJson = {
				'user_id': user_id,
				'file_name': file_name
			};
			var db = client.db('newnodejs');
			db.collection('gallery').find(insertJson).count(function(er, number) {
            	if(number == 0) {
            		db.collection('gallery').insertOne(insertJson);
            		response.json('Uploaded a pictrue successfully');
            		console.log('Uploaded a picture successfully');
            	}
            	else {
            		response.json('Upload Failed');
            		console.log('Requested picture already exists');
            	}
            });
		});

		app.post('/downloadG', (request, response, next) => {
			var post_data = request.body;
			var user_id = post_data.user_id;
			var db = client.db('newnodejs');
			db.collection('gallery').find({ 'user_id': user_id }).count(function(er, number) {
				if(number == 0)
				{
					response.json('');
					console.log('No picture to download!');
				}
				else
				{
					db.collection('gallery').find({ 'user_id': user_id }).toArray(function(err, result) {
						if (err) throw err;
						response.json(result)
						console.log('Downloaded gallery successfully');
					})
				}
			})
		});

		app.post('/uploadR', (request, response, next) => {
			var post_data = request.body;
			var user_id = post_data.user_id;
			var restaurant_list = post_data.restaurant_list;
			var option_list = post_data.option_list;

			var insertJson = {
				'user_id': user_id,
				'restaurant_list': restaurant_list,
				'option_list': option_list
			};
			var db = client.db('newnodejs');
			db.collection('restaurant').find({ 'user_id': user_id }).count(function(er, number) {
				if(number != 0) {
					db.collection('restaurant').deleteOne({ 'user_id': user_id });
				}
				db.collection('restaurant').insertOne(insertJson);
				response.json('Uploaded data successfully');
				console.log('Uploaded data successfully');
			});
		});

		app.post('/downloadR', (request, response, next) => {
			var post_data = request.body;
			var user_id = post_data.user_id;
			var db = client.db('newnodejs');
			db.collection('restaurant').find({ 'user_id': user_id }).count(function(er, number) {
				if(number == 0)
				{
					response.json('');
					console.log('No data to download!');
				}
				else
				{
					db.collection('restaurant').find({ 'user_id': user_id }).toArray(function(err, result) {
						if (err) throw err;
						response.json(result)
						console.log('Downloaded restaurant successfully');
					})
				}
			});
		});

		
		// Start Web Server
		app.listen(3000,() => {
			console.log('Connected to MongoDB server, WebService running on port 3000');
		})
	}
});