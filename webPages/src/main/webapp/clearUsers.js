/**
 * Created by jhark on 11/23/2015.
 */
function clearUsers(ip) {
    var result = "";
    $.ajax({
        url: "http://" + ip + ":4000/fileService/clearUsers",
        success: function (data) {
            result = data;
        },
        async: false
    });
    return result;
}
