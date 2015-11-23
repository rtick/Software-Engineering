/**
 * Created by jhark on 11/23/2015.
 */
function register(username,password,firstname,lastname,email,ip) {
    var result = "";
    var password = CryptoJS.AES.encrypt(password, password).toString();
     password = replaceAll("/", "_", password);
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
