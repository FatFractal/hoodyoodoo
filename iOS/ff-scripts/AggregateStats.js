var ff = require('ffef/FatFractal');

    function aggregateStats(json) {
        print("\n\n");
        print("\t*** hoodyoodoo AggregateStats extension\n");
        print("\t*** JSON data is " + json + "\n");
        data = JSON.parse(json);
        print("\t*** HTTP method is "       + data.httpMethod + "\n");
        print("\t*** HTTP request URI is "  + data.httpRequestUri + "\n");
        print("\t*** HTTP parameters are "  + JSON.stringify(data.httpParameters) + "\n");
        print("\t*** HTTP headers are "     + JSON.stringify(data.httpHeaders) + "\n");
        print("\t*** HTTP content is "      + data.httpContent + "\n");
        print("\t*** HTTP Cookies are "     + data.httpCookies + "\n");
        print("\t*** Logged-in user is "    + data.httpCookies['userGuid'] + "\n");
        print("\n\n");
        
        var createdBy = data.httpCookies['userGuid'];
        function StatsObject() {
            this.totalUsers = null;
            this.totalRatings = null;
            this.totalCelebrities = null;
            this.yourRatings = null;
            // If clazz is set the the FatFractal client library will use it to try to instantiate an 
            // appropriate client class. (For example in the hoodyoodoo iOS app we have a StatsObject class)
            // If clazz is not set, or the client library cannot locate a client class, then the client library 
            // will create a Map (Java) or Dictionary (obj-c) or whatever it's called in WinPho land
            this.clazz = "StatsObject";
            return this;
        }

        statsObject = new StatsObject();
        print("\t*** aggregateStats got here");
        statsObject.totalUsers = ff.getAllGuids("/FFUser").length;
        print("\t*** aggregateStats statsObject.totalUsers is "     + statsObject.totalUsers + "\n");
        statsObject.totalRatings = ff.getAllGuids("/WouldYa").length;
        print("\t*** aggregateStats statsObject.totalRatings is "     + statsObject.totalRatings + "\n");
        statsObject.totalCelebrities = ff.getAllGuids("/Celebrity").length;
        print("\t*** aggregateStats statsObject.totalCelebrities is "     + statsObject.totalCelebrities + "\n");
        statsObject.yourRatings = ff.getArrayFromUrl("/WouldYa/(createdBy eq '" + createdBy + "')").length;
        print("\t*** aggregateStats statsObject.yourRatings query is: (createdBy eq '" + createdBy + "') \n");
        print("\t*** aggregateStats statsObject.yourRatings is "     + statsObject.yourRatings + "\n");
        r = ff.response();
        r.result = JSON.stringify(statsObject);
        print("\t*** r.result is "     + r.result + "\n");
        r.responseCode = "200";
        r.statusMessage = "Getting the stuff you want in one round trip";
        r.mimeType = "application/json";
        print("\t*** r is "     + r + "\n");
        return r;
    }
    function sendPush(json) {
        data = JSON.parse(json);
        user = data.httpParameters['user'];
        message = data.httpParameters['message'];
        r = ff.response();
        if (user != null && message != null) {
            ff.sendPushNotifications ([user], message);
//            ff.sendPushNotifications (users, message);
            r.responseCode = "200";
            r.statusMessage = "Queued message " + message + " to be pushed to user " + user;
//            r.statusMessage = "Queued message " + message + " to be pushed (1) to user " + user + ", (2) to users " + users;
        }
        else {
            r.responseCode = "400";
            r.statusMessage = "You must supply a user and a message parameter. eg .../ff/ext/sendPush?user=gary&message=Hello, World" ;
        }
    }

exports.aggregateStats = aggregateStats;
exports.sendPush = sendPush;

