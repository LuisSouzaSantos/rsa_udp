Project: UDP using rsa encryption

Java 8 Application:

Just start the server (UdpServerStartup) first and then start the client (UdpClientStartup).


server generates public and private key.
client generates public and provate key.

The client sends the public key to the server and then the server sends its public key to the client.

So when client sends a message to the server, it will use the server public key to encrypt the message, 
the server will use its private key to decrypt the message, then encrypt it using the client's public 
key and send the message to the client. When the message arrives the client will decrypt it using its private key
