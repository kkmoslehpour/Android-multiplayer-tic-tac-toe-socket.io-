//server
//Keep the canonical game state
//Only respect a single playTurn message from the player who's turn it is
//Determine the winner (or that the game's a tie)


var express = require('express');
var app = express();
var path = require('path');
var server = require('http').Server(app);
var io = require('socket.io')(server);
var port = process.env.PORT || 3000;
var players = [];


server.listen(port, function () {
  console.log('Server listening at port %d', port);
});

//console.log("this is the __dirname: " + __dirname);
//app.use(express.static(path.join(__dirname, 'public')));

var roundCount = 0;
var isRoomFull = function(){
	return userCount === 2 ? true : false;
}

io.on('connection', function(socket){
	
	console.log("User connected!"); 
	socket.emit('socket id', {id: socket.id});
	socket.broadcast.emit('new player', {id: socket.id});
	socket.on('player turn', function(data){
		data.id = socket.id;
  		socket.broadcast.emit('player turn', data);
  		console.log("playerMoved: " + 
			" ID: " + data.id +
			" iButton: " + data.iButtonIndex +
			" jButton: " + data.jButtonIndex +
			" Player Type: " + data.playerType
		);
	});

	socket.on('player won', function(message){
	  	socket.broadcast.emit('player won', {
	  		message: message
	  	});
	});
	socket.on('draw game', function(message){
	  	socket.broadcast.emit('draw game', {
	  		message: message
	  	});
	});
});
