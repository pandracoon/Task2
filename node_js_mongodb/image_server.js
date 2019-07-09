var express = require('express');
var mongodb = require('mongodb');
var bodyParser = require('body-parser'); // json 형태로 파싱할꺼니까 모듈 추가
var formidable = require('formidable'); // form 태그 데이터들을 가져오는 모듈
var fs = require('fs-extra'); // 파일을 복사하거나 디렉토리 복사하는 모듈

var app = express();
app.use(bodyParser.json()); // body-parser 모듈을 사용해서 파싱 해줌
app.use(bodyParser.urlencoded({extended: true}));

var MongoClient = mongodb.MongoClient;
var url = 'mongodb://localhost:27017'

MongoClient.connect(url, function(err, client) {
	if(err)
		console.log('unable to connect to the mongoDB server.Error', err);
	else {

		app.post('/uploadG',function(req,res){ 
		    var user_id = "";
		    var filePath = "";
		    var form = new formidable.IncomingForm();

		    form.parse(req, function(err, fields, files) {
		    	user_id = fields.user_id;
		    })

		    form.on('end', function(fields, files) {
		    	for (var i = 0; i < this.openFiles.length; i++) {
		    		var temp_path = this.openFiles[i].path;
		    		var file_name = this.openFiles[i].name;
		    		var new_location = 'node_js_mongodb/public/images/'+name+'/';
		    		var db_new_location = 'images/'+name+'/';
		    		filePath = db_new_location + file_name;
		    		fs.copy(temp_path, new_location + file_name, function(err) {
		    			if(err) {
		    				console.error(err);
		    			}
		    		});
		    	}
			    var db = client.db('newnodejs');

			    db.collection('gallery').insertOne({ "name": name, "file_path": filePath }, function(err, doc) {

		    	});
			});
		});
	}
});
app.listen(3000); //server 구동 포트 localhost:3000 여기에 쓰입니다.
console.log("Server running on port 3000");