/**
 * Created by jhark on 11/23/2015.
 */
function verifyReset(test)
{
    var username = $.cookie("fileServiceUsername");
    var oldPassword = getCookie("fileServicePassword");
    document.cookie = "fileServiceUsername=;expires=Thu, 01 Jan 1970 00:00:01 GMT;";
    document.cookie = "fileServicePassword=;expires=Thu, 01 Jan 1970 00:00:01 GMT;";
    if (test == "test")
    {
        if (username == null)
        {
            return "Fail"
        }
        else
        {
            return "Pass"
        }
    }
    else
    {
        var array = new Array(username,oldPassword);
        return array;
    }
}