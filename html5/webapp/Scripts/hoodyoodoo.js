$(document).ready(function(){
    HoodyoodooViewModel.init();
});

var G_DEBUG = true;
var m_loadingImageElement = null;

// models
function Celebrity(data) {
    if (data) {
        this.clazz = data.clazz;
        this.guid = data.guid;
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.gender = data.gender;
        this.selectedCount = data.selectedCount;
        this.rejectedCount = data.rejectedCount;
        this.imageSrc = '.' + data.ffUrl + '/imageData';
    } else {
        this.clazz = "Celebrity";
        this.guid = null;
        this.firstName = null;
        this.lastName = null;
        this.gender = null;
        this.imageData = null;
        this.selectedCount = null;
        this.rejectedCount = null;
    }
    return this;
}

function WouldYa(data) {
    if (data) {
        this.clazz = data.clazz;
        this.selectedGuid = data.selectedGuid;
        this.rejectedGuid = data.rejectedGuid;
    } else {
        this.clazz = "WouldYa";
        this.selectedGuid = null;
        this.rejectedGuid = null;
    }
    return this;
}

function StatsObject(data) {
    if(data) {
        this.clazz = "StatsObject";
        this.totalUsers = new Number(data.totalUsers);
        this.totalCelebrities = new Number(data.totalCelebrities);
        this.totalRatings = new Number(data.totalRatings);
        this.yourRatings = new Number(data.yourRatings);
    } else {
        this.clazz = "StatsObject";
        this.totalUsers = null;
        this.totalCelebrities = null;
        this.totalRatings = null;
        this.yourRatings = null;
    }
    return this;
}

function ffRef(data) {
    if (data) {
        this.mime = data.mime;
        this.name = data.name;
        this.type = data.type;
        this.url = data.url;
    }
    else {
        this.mime = null;
        this.name = null;
        this.type = null;
        this.url = null;
    }
    return this;
}

function FileUploader() {
   // constants
   var MAX_FILE_SIZE = 300000;
   var CONTAINER_DIV = "fileUploaderContainer";
   var DROP_DIV = "fileUploaderDropDiv";
   var PREVIEW_IMG = "addCelebImage";
   var self = {
      containerDiv: null,
      containerClass: "",
      files: [],
      fileBytes: null,
      maxFileSize: 300000,
      multiple: false,
      containerWidth: 190, // 204 + 2*padding + border-top
      containerHeight: 241, // 204 + 2*padding + border-top
      init: function() {
         if(G_DEBUG) console.log("FileUploader().init() called");
         // avoid redundant call
         // containerDiv
         this.containerDiv = document.getElementById(CONTAINER_DIV);
         this.containerDiv.setAttribute("class",  + this.containerClass);
         // dropDiv
         var dropDiv = document.getElementById(DROP_DIV);
         dropDiv.addEventListener('drop', drop, false);
         dropDiv.addEventListener("dragenter", dragEnter, false);
         dropDiv.addEventListener("dragexit", dragExit, false);
         dropDiv.addEventListener("dragover", dragOver, false);
         
         var previewImg = document.getElementById(PREVIEW_IMG);
         previewImg.src = "Images/addphoto.png";

         function dragEnter(evt) {
            evt.stopPropagation();
            evt.preventDefault();
         }

         function dragExit(evt) {
            evt.stopPropagation();
            evt.preventDefault();
         }

         function dragOver(evt) {
            evt.stopPropagation();
            evt.preventDefault();
            evt.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
         }
         
         function drop(evt) {
            if(G_DEBUG) console.log("FileUploader().init() drop event detected");
            evt.stopPropagation();
            evt.preventDefault();
            self.files = evt.dataTransfer.files;
            if(G_DEBUG) console.log("FileUploader().init() drop event self.files now has " + self.files.length + " files.");

            file = self.files[0]; 
            if(file.fileSize > this.maxFileSize) {
               if(G_DEBUG) console.error("FileUploader().handleFiles determined file size too large, aborting.");
               return;
            }

            // Only process image files. 
            var imageType = /image.*/; 

            if(!file.type.match(imageType)) {
               if(G_DEBUG) console.error("FileUploader().handleFiles did not recognize file as an image, aborting.");
               return; 
            } 

            var reader = new FileReader(); 

            reader.onerror = function(e) {
                alert('Error code: ' + e.target.error); 
            }; 

            // Create a closure to capture the file information. 
            reader.onload = (function(aFile) {
                return function(evt) { 
                    //dropDiv.setAttribute("class","hasImage");
                    previewImg.src = evt.target.result; 
                    HoodyoodooViewModel.addImage();
                } 
            })(file); 
            reader.readAsDataURL(file); 
         } 
      },

      uploaderElement: function() {
         if(this.containerDiv) return this.containerDiv;
         else return false;
      },

      getFiles: function() {
         if(G_DEBUG) console.log("FileUploader().getFiles() called.");
         if(self.files.length > 0) {
            if(G_DEBUG) console.log("FileUploader.getFiles.found: " + self.files.length + " files.");
            return self.files;
         }
         else return null;
      },
      
      getByteArray: function(file) {
         if(G_DEBUG) console.log("FileUploader().getByteArray() called.");
         if(self.fileBytes > 0) {
            if(G_DEBUG) console.log("FileUploader.getByteArray.found byteArray with: " + self.fileBytes);
            return self.fileBytes;
         }
         else return null;
      }
   }
   return self;
}

var HoodyoodooViewModel = (function() {
    var ff =  new FatFractal();
    
    var previousPage = "";

    var m_currentGender = "female";

    var m_leftCelebrity = null;

    var m_rightCelebrity = null;

    var m_celebrity = null;

    var m_topCeleb = null;

    var m_genderButton = null;

    var m_leftCelebrityButton = null;

    var m_leftCelebrityLabel = null;

    var m_rightCelebrityButton = null;

    var m_rightCelebrityLabel = null;

    var m_topResponseLabel = null;

    var m_bottomResponseLabel = null;

    var m_playAgainButton = null;

    var m_doneButton = null;
    
    var m_addCelebImage = null;

    var m_topCelebImage = null;

    var m_topCelebrityLabel = null;

    var m_selectedCountLabel = null;

    var m_rejectedCountLabel = null;

    var m_ratingCountLabel = null;

    var m_userCountLabel = null;

    var m_celebrityCountLabel = null;

    var m_yourRatingCountLabel = null;

    var m_firstNameField = null;

    var m_lastNameField = null;
    
    var m_wouldYa = null;
    
    var self = {
        init: function() {
            ff.setDebug(G_DEBUG);
            m_genderButton = document.getElementById("genderButton");
            m_leftCelebrityButton = document.getElementById("leftCelebrityButton");
            m_leftCelebrityLabel = document.getElementById("leftCelebrityLabel");
            m_rightCelebrityButton = document.getElementById("rightCelebrityButton");
            m_rightCelebrityLabel = document.getElementById("rightCelebrityLabel");
            m_topResponseLabel = document.getElementById("topResponseLabel");
            m_bottomResponseLabel = document.getElementById("bottomResponseLabel");
            m_playAgainButton = document.getElementById("playAgainButton");
            m_doneButton = document.getElementById("doneButton");
            m_topCelebrityLabel = document.getElementById("topCelebrityLabel");
            m_selectedCountLabel = document.getElementById("selectedCountLabel");
            m_rejectedCountLabel = document.getElementById("rejectedCountLabel");
            m_ratingCountLabel = document.getElementById("ratingCountLabel");
            m_userCountLabel = document.getElementById("userCountLabel");
            m_celebrityCountLabel = document.getElementById("celebrityCountLabel");
            m_yourRatingCountLabel = document.getElementById("yourRatingCountLabel");
            m_firstNameField = document.getElementById("firstNameField");
            m_lastNameField = document.getElementById("lastNameField");
            m_topCelebImage = document.getElementById("topCelebImage");
            m_addCelebImage = document.getElementById("addCelebImage");
            m_loadingImageElement = document.getElementById("imgAjaxLoader");
            if(m_topResponseLabel) m_topResponseLabel.style.visibility="hidden";
            if(m_bottomResponseLabel) m_bottomResponseLabel.style.visibility="hidden";
            if(m_playAgainButton) m_playAgainButton.style.visibility="hidden";
            m_celebrity = new Celebrity();
            loader = new FileUploader();
            loader.init();
            this.loadCelebrities();
        },

        loadCelebrities: function() {
            if(G_DEBUG) console.log("loadCelebrities() called");
            m_playAgainButton.style.visibility="hidden";
            self.showLoading(true);
            ff.getObjFromUri("/ff/resources/Celebrity/(gender eq '" + m_currentGender + "' and guid eq random(guid))",
                function(leftCelebrity){
                    self.showLoading(false);
                    if(leftCelebrity) {
                        m_leftCelebrity = new Celebrity(leftCelebrity);
                        m_leftCelebrityButton.setAttribute('src', m_leftCelebrity.imageSrc);
                        m_leftCelebrityLabel.innerHTML = leftCelebrity.firstName + " " + leftCelebrity.lastName;
                        ff.getObjFromUri("/ff/resources/Celebrity/(gender eq '" + m_currentGender + "' and guid ne '" + leftCelebrity.guid + "' and guid eq random(guid))",
                            function(rightCelebrity){
                                if(rightCelebrity) {
                                    m_rightCelebrity = new Celebrity(rightCelebrity);
                                    m_rightCelebrityButton.setAttribute('src', m_rightCelebrity.imageSrc);
                                    m_rightCelebrityLabel.innerHTML = rightCelebrity.firstName + " " + rightCelebrity.lastName;
                                } else if(G_DEBUG) console.log("No celebrity found for m_rightCelebrity");
                            }, function(statusCode, responseText){
                                console.error("Error "+ statusCode + ": " + JSON.parse(responseText).statusMessage);
                            }
                        );
                    } else if(G_DEBUG) console.log("No celebrity found for m_leftCelebrity, skipping get for m_rightCelebrity");

                }, function(statusCode, responseText){
                    self.showLoading(false);
                    console.error("Error "+ statusCode + ": " + JSON.parse(responseText).statusMessage);
                }
            );
        },

        toggleGender: function() {
            if(G_DEBUG) console.log("toggleGender() called");
            if(m_currentGender == "male") {
                m_genderButton.setAttribute("src", "Images/button_gender_female.png");
                m_currentGender = "female";
            } else {
                m_genderButton.setAttribute("src", "Images/button_gender_male.png");
                m_currentGender = "male";
            }
            if(G_DEBUG) console.log("toggleGender() set m_currentGender to: " + m_currentGender);
            self.loadCelebrities();
        },

        celebrityWasPicked: function(sender) {
            if(G_DEBUG) console.log("celebrityWasPicked() called");
    	    var wouldYa = new WouldYa();
            if(sender == m_leftCelebrityButton && m_leftCelebrity) {
                if(G_DEBUG) console.log("celebrityWasPicked() found m_leftCelebrity selected");
                wouldYa.selectedGuid = m_leftCelebrity.guid;
                wouldYa.rejectedGuid = m_rightCelebrity.guid;
                //[[leftCelebrityButton layer] setBorderColor:[UIColor greenColor].CGColor];
                //[[rightCelebrityButton layer] setBorderColor:[UIColor redColor].CGColor];
                //m_topResponseLabel.innerHTML = "Woo Hoo! Thanks!";
                //m_bottomResponseLabel.innerHTML = "Awww Man!";
                m_wouldYa = wouldYa;
                self.persistWouldYa();
            } else if(sender == m_rightCelebrityButton && m_rightCelebrity) {
                if(G_DEBUG) console.log("celebrityWasPicked() found m_rightCelebrityButton selected");
                wouldYa.selectedGuid = m_rightCelebrity.guid;
                wouldYa.rejectedGuid = m_leftCelebrity.guid;
                //[[leftCelebrityButton layer] setBorderColor:[UIColor redColor].CGColor];
                //[[rightCelebrityButton layer] setBorderColor:[UIColor greenColor].CGColor];
                //m_bottomResponseLabel.innerHTML = "Woo Hoo! Thanks!";
                //m_topResponseLabel.innerHTML = "Awww Man!";
                m_wouldYa = wouldYa;
                self.persistWouldYa();
            } else if(G_DEBUG) console.log("celebrityWasPicked() could not determine which celebrity was picked");
        },
    
        persistWouldYa: function() {
            if(G_DEBUG) console.log("persistWouldYa() called");
            m_playAgainButton.style.visibility="visible";
            if(!ff.loggedIn()) {
                if(G_DEBUG) console.log("persistWouldYa() not logged in");
                $("#wouldYaLoginPopup").popup('open');
            } else {
                //m_topResponseLabel.style.visibility="visible";
                //m_bottomResponseLabel.style.visibility="visible";
                self.showLoading(true);
                if(m_wouldYa) {
                    ff.createObjAtUri(m_wouldYa, "/WouldYa",
                        function(data) {
                            self.showLoading(false);
                            // what to do here??
                        }, function(statusCode, responseText){
                            self.showLoading(false);
                            console.error("Error "+ statusCode + ": " + JSON.parse(responseText).statusMessage);
                        }
                    );
                } else console.error("WouldYaViewController persistWouldYa failed: m_wouldYa is null");
            }
        },

        login: function(callback) {
            if(callback == 'persistWouldYa') {
                self.showLoading(true);
                var userName = document.getElementById("wouldYaUsernameField").value;
                var password = document.getElementById("wouldYaPasswordField").value;
                if(userName && password) {
                    ff.login(userName, password, 
                        function(user) {
                            self.showLoading(false);
                            $("#wouldYaLoginPopup").popup('close');
                            self.persistWouldYa();
                        }, function(statusCode, responseText){
                            self.showLoading(false);
                            console.error("Error "+ statusCode + ": " + JSON.parse(responseText).statusMessage);
                        }
                    );
                } else {
                    alert("You must provide both username and password");
                }
            } else if(callback == 'addCelebrity') {
                self.showLoading(true);
                var userName = document.getElementById("addCelebUsernameField").value;
                var password = document.getElementById("addCelebPasswordField").value;
                if(userName && password) {
                    ff.login(userName, password, 
                        function(user) {
                            self.showLoading(false);
                            $("#addCelebLoginPopup").popup('close');
                            self.addCelebrity();
                        }, function(statusCode, responseText){
                            self.showLoading(false);
                            console.error("Error "+ statusCode + ": " + JSON.parse(responseText).statusMessage);
                        }
                    );
                } else {
                    alert("You must provide both username and password");
                }
            } else if(callback == 'getTopCelebrity') {
                self.showLoading(true);
                var userName = document.getElementById("topCelebUsernameField").value;
                var password = document.getElementById("topCelebPasswordField").value;
                if(userName && password) {
                    ff.login(userName, password, 
                        function(user) {
                            self.showLoading(false);
                            $("#topCelebLoginPopup").popup('close');
                            self.getTopCelebrity();
                        }, function(statusCode, responseText){
                            self.showLoading(false);
                            console.error("Error "+ statusCode + ": " + JSON.parse(responseText).statusMessage);
                        }
                    );
                } else {
                    alert("You must provide both username and password");
                }
            }
        },
    
        getTopCelebrity: function() {
            if(G_DEBUG) console.log("getTopCelebrity() called");
            self.showLoading(true);
            ff.getObjFromUri("/TopCelebrity",
                function(topCeleb) {
                    self.showLoading(false);
                    if(topCeleb) {
                        var tc = new Celebrity(topCeleb);
                        m_topCelebImage.setAttribute('src', tc.imageSrc);
                        m_topCelebrityLabel.innerHTML = tc.firstName + " " + tc.lastName;
                        if(tc.selectedCount) m_selectedCountLabel.innerHTML = tc.selectedCount;
                        if(tc.rejectedCount) m_rejectedCountLabel.innerHTML = tc.rejectedCount;
                    } else if(G_DEBUG) console.log("getTopCelebrity() found no top celebrity");
                    getStats();
                }, function(statusCode, responseText){
                    self.showLoading(false);
                    console.error("Error "+ statusCode + ": " + JSON.parse(responseText).statusMessage);
                    getStats();
                }
            );

            function getStats() {
                if(G_DEBUG) console.log("getTopCelebrity() called");
                if(!ff.loggedIn()) {
                    if(G_DEBUG) console.log("getStats() not logged in");
                    $("#topCelebLoginPopup").popup('open');
                    //showLoginWithDelegate:self action:@selector(getStats) 
                    //message:"Please Login"];
                } else {
                    self.showLoading(true);
                    ff.getObjFromExtension("/ff/ext/Stats",
                        function(statsObject) {
                            self.showLoading(false);
                            m_ratingCountLabel.innerHTML = statsObject.totalRatings;
                            m_userCountLabel.innerHTML = statsObject.totalUsers;
                            m_celebrityCountLabel.innerHTML = statsObject.totalCelebrities;
                            m_yourRatingCountLabel.innerHTML = statsObject.yourRatings;
                        }, function(statusCode, responseText){
                            self.showLoading(false);
                            console.error("Error "+ statusCode + ": " + JSON.parse(responseText).statusMessage);
                        }
                    );
                }
            }
        },

        addImage:function() {
            if(G_DEBUG) console.log("addImage() called");
            var picker = new FileUploader();
               var reader = new FileReader();
               reader.onerror = function(evt) {
                  var message;

                  // REF: http://www.w3.org/TR/FileAPI/#ErrorDescriptions
                  switch(evt.target.error.code) {
                     case 1:
                        message = file.name + " not found.";
                        break;

                     case 2:
                        message = file.name + " has changed on disk, please re-try.";
                        break;

                     case 3:
                        messsage = "Upload cancelled.";
                        break;

                     case 4:
                     message = "Cannot read " + file.name + ".";
                     break;

                     case 5:
                     message = "File too large for browser to upload.";
                     break;
                  }
               }
               //reader.onloadend = function(evt){
               reader.onload = function(evt){
                  m_celebrity.imageData = evt.target.result;
                  if(G_DEBUG) console.log("HoodyoodooViewModel.addImage.onloadend called");
               }
               reader.readAsArrayBuffer(file);
        },

        doneAction: function() {
            if(G_DEBUG) console.log("HoodyoodooViewModel.doneAction() called");
            if(m_firstNameField.value) m_celebrity.firstName = m_firstNameField.value;
            if(m_lastNameField.value) m_celebrity.lastName = m_lastNameField.value;
            self.addCelebrity();
        },

        addCelebrity: function() {
            if(G_DEBUG) console.log("HoodyoodooViewModel.addCelebrity() called");
            // check logged in
            if(!ff.loggedIn()) {
                if(G_DEBUG) console.log("addCelebrity() not logged in");
                $("#addCelebLoginPopup").popup('open');
                //showLoginWithDelegate:self action:@selector(getStats) 
                //message:"Please Login"];
            } else {
                // check requisite info exists
                if ((m_celebrity.firstName == null) || (m_celebrity.lastName == null)) {
                    alert("Add Celebrity Failed. You must provide first and last name for this celebrity");
                }
                else if (m_celebrity.imageData == null) {
                    alert("Add Celebrity Failed. You must provide an image for this celebrity");
                } 
                else if (m_celebrity.gender == null) {
                    self.addGender();
                } 
                else {
                    console.log("CelebrityViewController createCelebrity: all requisite items exist.");
                    console.log("CelebrityViewController createCelebrity newCelebString = " + JSON.stringify(m_celebrity));
                    self.showLoading(true);
                    ff.createObjAtUri(m_celebrity, "/Celebrity",
                        function(data) {
                            self.showLoading(false);
                            m_firstNameField.value = "";
                            m_lastNameField.value = "";
                            m_addCelebImage.src = "Images/addphoto.png";
                            m_celebrity = new Celebrity();
                        }, function(statusCode, responseText){
                            self.showLoading(false);
                            console.error("Error "+ statusCode + ": " + JSON.parse(responseText).statusMessage);
                        }
                    );
                }
            }
        },

        addGender: function(gender) {
            if(G_DEBUG) console.log("addGender() called");
            $("#addGenderPopup").popup('close');
            if(!gender) {
                $("#addGenderPopup").popup('open');
            } else if(gender == "male") {
                m_celebrity.gender = "male";
                self.addCelebrity();
            } else if(gender == "female") {
                m_celebrity.gender = "female";
                self.addCelebrity();
            } else if(G_DEBUG) console.error("addGender() could not process the gender selection");
        },

        showLoading:function(show) {
            if(G_DEBUG) console.log("self.showLoading() called");
            if(m_loadingImageElement == null) {
                if(G_DEBUG) console.log("self.showLoading() did not find m_loadingImageElement, adding a new one");
                var i = document.createElement('img');
                var b = document.getElementById("body");
                i.id = "imgAjaxLoader";
                i.setAttribute("src", "https://ajax.aspnetcdn.com/ajax/jquery.mobile/1.1.0/images/ajax-loader.gif");
                i.setAttribute("class", "imgAjaxLoader");
                b.appendChild(i);
                m_loadingImageElement = document.getElementById("imgAjaxLoader");
            }
            if(show){
                m_loadingImageElement.style.display = 'block';
            } else {
                m_loadingImageElement.style.display = 'none';
            }
        }
    }
    return self;
})();

