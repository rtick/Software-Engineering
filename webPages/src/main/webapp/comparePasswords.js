/**
 * Created by jhark on 11/23/2015.
 */
function comparePasswords(enteredPassword, password)
{
    /*This is what we will change to pass our first regression test, being able to log in
    password = replaceAll("_", "/", password);
    password = CryptoJS.AES.decrypt(password, enteredPassword).toString(CryptoJS.enc.Latin1);*/
    enteredPassword = CryptoJS.MD5(enteredPassword).toString();
    if (enteredPassword == password)
    {
        return "Confirmed";
    }
    else
    {
        return "Unconfirmed";
    }
}
function replaceAll(find, replace, str) {
    return str.replace(new RegExp(find, 'g'), replace);
}

