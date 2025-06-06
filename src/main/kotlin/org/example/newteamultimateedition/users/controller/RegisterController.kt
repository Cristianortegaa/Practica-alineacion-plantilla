package org.example.newteamultimateedition.users.controller

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.Stage
import org.example.newteamultimateedition.routes.RoutesManager
import org.example.newteamultimateedition.users.exception.UsersException
import org.example.newteamultimateedition.users.models.User
import org.example.newteamultimateedition.users.service.UsersServiceImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named


import org.mindrot.jbcrypt.BCrypt

/**
 * Controlador de Registro de usuarios
 * @property dao DAO que interviene con la base de datos
 * @property acercaDeButton [Button] Boton 'Acerca de'
 * @property errorMessage [Label] Label que saca un mensaje de error
 * @property passwordText [PasswordField] Campo que recoge la contraseña
 * @property userText [TextField] Campo que recoge el usuario
 * @property registerButton [Button] boton que procesa el inicio de sesion
 */
class RegisterController: KoinComponent {

    private val dao: UsersServiceImpl by inject()

    private val cache: Cache<Long,User> by inject(named("usersCache"))

    @FXML
    lateinit var acercaDeButton: Button
    @FXML
    lateinit var errorMessage: Label
    @FXML
    lateinit var passwordText: PasswordField
    @FXML
    lateinit var userText: TextField
    @FXML
    lateinit var registerButton: Button
    @FXML
    lateinit var passwordConfirmationText: PasswordField
    @FXML
    lateinit var passwordMatches: Label
    @FXML
    lateinit var toLogin: Hyperlink

    @FXML
    fun initialize() {
        initEvents()
    }

    /**
     * Inicia los eventos de los componentes de la interfaz
     */
    private fun initEvents() {

        acercaDeButton.setOnAction {
            RoutesManager.initAboutStage()
        }

        userText.textProperty().addListener { _, oldValue, newValue ->
            if (oldValue != newValue) {
                errorMessage.text = ""
                userText.style = "-fx-border-color: rgba(0,0,0,0);"
                passwordText.style = "-fx-border-color: rgba(0,0,0,0);"
            }
        }
        passwordText.textProperty().addListener { _, oldValue, newValue ->
            if (oldValue != newValue) {
                errorMessage.text = ""
                userText.style = "-fx-border-color: rgba(0,0,0,0);" +
                        "-fx-border-width: 1px;"
                passwordText.style = "-fx-border-color: rgba(0,0,0,0);"
            }
        }
        passwordConfirmationText.textProperty().addListener { _, _, _ ->
            if(passwordConfirmationText.text != passwordText.text) {
                passwordMatches.style = "-fx-text-fill: #cea300;"
                passwordMatches.text = "Las contraseñas no coinciden"
            }
            else {
                passwordMatches.text = ""
            }
        }
        registerButton.setOnAction {
            registerUser()
        }

        toLogin.setOnAction {
            RoutesManager.initLoginStage(toLogin.scene.window as Stage)
        }
    }

    /**
     * Función que se encarga de registrar al usuario o mostrar un error en caso de fallo
     */
    private fun registerUser(){
        val result = validation(userText.text.toString(), passwordText.text.toString(), passwordConfirmationText.text)
        if (result.isErr) showUserError(result.error.message)
        else {
            if (dao.save(result.value).isErr){
                showUserError("La base de datos ha explotado por favor reinicie la aplicacion")
            }else{
                cache.put(0L,result.value)
                RoutesManager.initUserStage(toLogin.scene.window as Stage)
            }
        }
    }

    /**
     * Valida el formulario de registro
     * @param userName Nombre de usuario
     * @param password Contraseña del usuario sin hashear
     * @param passwordConfirmation Confirmación de la contraseña del usuario.
     * @return [Result] de [User] en caso correcto o de [UsersException] en caso incorrecto o de fallo
     */
    private fun validation(userName: String, password: String, passwordConfirmation: String) : Result<User,UsersException> {
        val encrypt= BCrypt.hashpw(password, BCrypt.gensalt(12))
        if (userName.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()){
            return Err(UsersException.ContraseniaEquivocadaException("Por favor, rellena los campos"))
        }
        if (password!= passwordConfirmation){
            return Err(UsersException.ContraseniaEquivocadaException("Las contraseñas no son iguales."))
        }
        return Ok(User(userName, encrypt))
    }

    /**
     * Muestra el error al usuario cambiando los colores de la interfaz y mostrando el mensaje de error
     * @param error El mensaje de error
     */
    private fun showUserError(error: String) {
        errorMessage.style = "-fx-text-fill: #FF2C2C;"
        userText.style = "-fx-border-color: #FF2C2C;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 3"
        passwordText.style = "-fx-border-color: #FF2C2C;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 3"
        errorMessage.text = error
    }
}

