/**
 * Created by jhark on 11/23/2015.
 */
function register(username,password,firstname,lastname,email,ip) {
    var result = "";
    //This is what we need to pass our feature implementation step, we send an MD5 sum as opposed to encryption
    /*var password = CryptoJS.AES.encrypt($("#password").val(), $("#password").val()).toString();
     password = replaceAll("/", "_", password);*/

    var password = CryptoJS.MD5(password).toString();

    $.ajax({
        url: "http://" + ip + ":4000/fileService/registerUser/" + username + "/" + password + "/" + firstname + "/" + lastname + "/" + email + "/" + ip,
        success: function (data) {
            result = data;
            if (data == "User Added")
            {
                result =  password;
            }
            else
            {
                result = "Fail";
            }
        },
        async: false
    });
    return result;
}
function replaceAll(find, replace, str) {
    return str.replace(new RegExp(find, 'g'), replace);
}
