/**
 * Created by jhark on 11/23/2015.
 */
function reset(username,oldPassword,newPassword,ip) {
    var result = "";
     newPassword = CryptoJS.AES.encrypt(newPassword, newPassword).toString();
     newPassword = replaceAll("/", "_", newPassword);
    $.ajax({
        url: "http://" + ip + ":4000/fileService/resetPass/" + username + "/" + oldPassword + "/" + newPassword,
        success: function (data) {
            if (data == "Successful")
            {
                result = newPassword;
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
