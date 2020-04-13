body{
	background-color:#F9F9F9;
	text-align:center;
	color:#333333;
}

.header{
	overflow: hidden;
	background-color:#333;
	left:0;
	top:0;
	box-shadow:0 0 8px black;
	z-index:99;
	height:10%;
	width:100%;
	position:fixed;
}

#logo {
	cursor: pointer;
	float:left;
	margin-left:1%;
	font-weight:100;
	padding:1%;
	color:white;
}

#menu{
	float:left;
	margin-left:3%;
	font-weight:100;
}

.btnMenu{
	cursor:pointer;
	padding:1%;
	transition:all 0.3s ease-in-out;
}

.btnMenu:hover{
	font-weight:bold;
}

input[type=text], input[type=email], input[type=password], textarea, select{
	width:300px;
	margin: 0 auto;
	text-align: center;
	box-shadow: inset 0 0 4px rgba(0, 0, 0, 0.35);
}

.form-control{
	height:50px;
	width:300px;
	border-radius:8px;
	box-shadow:inset 1px 2px 3px #888;
	border:1px solid black;
	text-align:center;
	font-size:24px;
	margin:0 auto;
	margin-left:5px;
	margin-right:5px;
	transition:all 0.2s ease-in-out;
}

.form-control:focus{
	border-color:#212121;
	color:#212121;
}

.cButton {
	cursor:pointer;
	color:rgba(255, 255, 255, 1);
	text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.5);
	display: inline-block;
	border-radius: 0.3em;
	border: 1px solid;
	border-color: #bbbbbb #a2a2a2 #888888;
	background-color: #333333;
	background-image: linear-gradient(#333, #000000);
	box-shadow: 0 0.1em 0.5em rgba(0, 0, 0, 0.1), 0 0.1em 0.2em rgba(0, 0, 0, 0.3), 0 -0.1em 0.07em rgba(0, 0, 0, 0.3) inset, 0 0.1em 0.07em rgba(255, 255, 255, 0.2) inset;
	transition: all 100ms;
	padding:10px;
	padding-top:5px;
	padding-bottom:5px;
	font-size:1.1em;
	font-weight:500;
	width:300px !important;
}

.cButton:hover {
	box-shadow: 0 0.1em 0.5em rgba(0, 0, 0, 0.4), 0 0.1em 0.2em rgba(0, 0, 0, 0.4), 0 -0.1em 0.07em rgba(0, 0, 0, 0.3) inset, 0 0.1em 0.07em rgba(255, 255, 255, 0.2) inset;
}

.cButton:active {
	border-color: #a2a2a2 #bbbbbb #eeeeee;
	background-image: linear-gradient(#000000, #333);
	box-shadow: 0 0.1em 0.2em rgba(0, 0, 0, 0.1) inset, 0 0.1em 0.1em rgba(0, 0, 0, 0.2) inset, 0.05em 0 0.07em rgba(0, 0, 0, 0.2) inset, -0.05em 0 0.07em rgba(0, 0, 0, 0.2) inset;
	transition: all 10ms;
}

.decoTable{
	box-shadow:0 0 8px #888;
	margin:0 auto;
	border-radius:4px;
}

table {
  border-collapse: collapse;
  width: 100%;
}

th, td {
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {background-color: #f2f2f2;}

.tableText{
	text-align:center;
	font-weight:bold;
	font-size:1em;
	padding:5px;
}