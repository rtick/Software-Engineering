/**
 * Created by jhark on 11/23/2015.
 */
function login(username, ip) {
    var result = "";
    $.ajax({
        url: "http://" + ip + ":4000/fileService/login/" + username,
        success: function (data) {
            result = data;
        },
        async: false
    });
    return result;
}
     