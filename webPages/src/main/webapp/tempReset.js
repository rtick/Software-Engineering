/**
 * Created by jhark on 11/23/2015.
 */
    function tempReset(username, password) {
    document.cookie = "fileServiceUsername=" + username + ";path=/";
    document.cookie = "fileServicePassword=" + password + ";path=/";
    return "Success"
}