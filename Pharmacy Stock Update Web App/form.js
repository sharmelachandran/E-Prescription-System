var firebaseConfig = {
    apiKey: "AIzaSyCufe4_ay3TemTRveGnz5ddq6u-tCSPCeI",
    authDomain: "eprescription-1790b.firebaseapp.com",
    databaseURL: "https://eprescription-1790b.firebaseio.com",
    projectId: "eprescription-1790b",
    storageBucket: "eprescription-1790b.appspot.com",
    messagingSenderId: "798527357815",
    appId: "1:798527357815:web:b6a75abd79bdeece07d772",
    measurementId: "G-2G6M4SD5NZ"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  firebase.analytics();
  firebase.auth.Auth.Persistence.LOCAL
  function signin()
  {
      var email=document.getElementById("email").value;
      var password=document.getElementById("password").value;
      if(email!=""&&password!="")
      {
      var result =firebase.auth().signInWithEmailAndPassword(email, password);
       result.catch(function(error)
      {
      var errorcode=error.code;
      var errorMessage=error.message;
      console.log(errorcode);
      onsole.log(errorMessage);
      window.alert("Message :"+errorMessage);
      });

      firebase.auth().onAuthStateChanged(function(user){
        if(user)
        {
            window.location.href="mainpage.html";
        }
     });
   
     }
      else{
        window.alert("please enter all the fields");
      }
  }

     


  function signup()
  {
    var name=document.getElementById("name").value;
    var phone=document.getElementById("phone").value;
    var address=document.getElementById("address").value;
   
   var email=document.getElementById("email").value;
   var password=document.getElementById("password").value;
   if(email!=""&&password!="")
   {
   var result =firebase.auth().createUserWithEmailAndPassword(email, password);
   firebase.database().ref("pharmacy").child(firebase.auth().currentUser.uid).set({
    pharmacyname:name,
    pharmacynum:phone,
  pharmacyaddress:address,
mailid:email });
  
   result.catch(function(error)
   {
   var errorcode=error.code;
   var errorMessage=error.message;
   console.log(errorcode);
   onsole.log(errorMessage);
   window.alert("Message :"+errorMessage);
  });

  firebase.auth().onAuthStateChanged(function(user){
      if(user)
      {
          window.location.href="mainpage.html";
      }
  });
   
   
   }
   else{
     window.alert("please enter all the fields");
   }
       
 
 } 

 function signuppage()
 {
   window.location.href="index2.html";
 }
 function loginpage()
 {
   window.location.href="index.html";
 }