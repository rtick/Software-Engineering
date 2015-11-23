/**
 * Created by jhark on 11/23/2015.
 */
function confirmUser(username,ip) {
    var result = "";
    $.ajax({
        url: "http://" + ip + ":4000/fileService/confirmUser/" + username + "/" + ip,
        success: function (data) {
            result = data;
        },
        async: false
    });
    return result;
}
