# 📡 ESP32 Bluetooth Commander - Seu Portal IoT sem Fios!  

**Conecte, controle e comunique-se com dispositivos ESP32 via Bluetooth de forma simples e eficiente!**  

---  

## 🚀 **O que é o ESP32 Bluetooth Commander?**  
Um aplicativo Android desenvolvido com ❤️ no **Android Studio** para facilitar a comunicação entre seu smartphone e dispositivos **ESP32** via **Bluetooth**, ideal para projetos IoT, automação residencial, robótica e muito mais!  

### 🔥 **Recursos Incríveis**  
✅ **Liga/Desliga Bluetooth** com um toque  
✅ **Lista dispositivos pareados** e disponíveis  
✅ **Conexão rápida e estável** com ESP32  
✅ **Comunicação bidirecional** (envia e recebe dados)  
✅ **Contador de mensagens** para testes e monitoramento  

---  

## 🛠️ **Como Usar?**  

1. **Ative o Bluetooth** no seu Android  
2. **Pareie seu ESP32** (certifique-se de que está visível)  
3. **Selecione o dispositivo** na lista  
4. **Conecte-se** e comece a trocar dados!  

🔹 **Enviando dados?** O app manda mensagens automáticas (como `"Olá ESP32: Contagem: X"`) a cada segundo!  
🔹 **Recebendo dados?** As mensagens do ESP32 aparecem em **Toast** na tela!  

---  

## 📝 **Código & Personalização**  

- **Linguagem:** Kotlin  
- **Bibliotecas:** Android Bluetooth API  
- **UUID Padrão:** `00001101-0000-1000-8000-00805F9B34FB` (SPP)  

**Quer modificar?**  
- Altere `dadosParaEnviar` em `gerenciarConexao()` para enviar comandos personalizados  
- Adapte o tratamento de dados recebidos para seu projeto  

---  

## 🚨 **Avisos Importantes**  
⚠️ Requer permissão de **Bluetooth** no Android  
⚠️ Funciona apenas com dispositivos que suportam **SPP (Serial Port Profile)**  
⚠️ Testado em ESP32 com firmware padrão para comunicação serial  

---  

## 🌟 **Por que usar este app?**  
- **Simplicidade:** Interface intuitiva e código limpo  
- **Open Source:** Sinta-se à vontade para clonar e melhorar!  
- **IoT Friendly:** Perfeito para prototipagem rápida  

---  

## 📥 **Download & Contribuição**  
🔗 *Link para o APK (em breve)* | *Repositório no GitHub (em breve)*  

**Desenvolvido por Vinicius Moreira Nascimento** – Um apaixonado por IoT e Android!  

---  

💡 **Dica:** Combine com um ESP32 programado para responder aos comandos e crie um sistema IoT poderoso!  

**🎉 Conecte-se ao futuro, sem fios!** 🎉  

---  

> *"Talk is cheap. Show me the code." – Linus Torvalds*  

---  

**📜 Licença:** MIT  
