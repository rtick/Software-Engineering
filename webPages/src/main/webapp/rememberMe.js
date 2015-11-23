/**
 * Created by jhark on 11/23/2015.
 */
function rememberMe(username, password, isChecked, remembered){
    if (isChecked) {
        if (!remembered) {
            var currentDate = new Date();
            currentDate.setDate(currentDate.getDate() + 7);
            document.cookie = "fileServiceUsernameWeek=" + username + ";expires=" + currentDate.toUTCString() + ";path=/";
            document.cookie = "fileServicePasswordWeek=" + password + ";expires=" + currentDate.toUTCString() + ";path=/";
        }
        else
        {
            document.cookie = "fileServiceUsername=" + username + ";path=/";
            document.cookie = "fileServicePassword=" + password+ ";path=/";
        }
    }
    else {
        if (remembered) {
            document.cookie = "fileServiceUsernameWeek=;expires=Thu, 01 Jan 1970 00:00:01 GMT;";
            document.cookie = "fileServicePasswordWeek=;expires=Thu, 01 Jan 1970 00:00:01 GMT;";
        }
        document.cookie = "fileServiceUsername=" + username + ";path=/";
        document.cookie = "fileServicePassword=" + password + ";path=/";
    }
}