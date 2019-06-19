# app-spotify!

API que disponibiliza e **músicas** e **artistas** utilizando a plataforma **Spotify**. 
- A API foi desenvolvida para ser integrada com o microserviço **app-music**.

![enter image description here](https://lh3.googleusercontent.com/Yy4rFE3UmDspCR_3u6CxRTneGOxi4EmeiAYGbrwGecdrd8nBKI1hbLsMn-qVmTdNfH_fdsyS3XwA "app-spotify-modelo")


Parte do modelo arquitetural representa a comunicação com a API Spotify. O app-music faz uma requisição ao recurso "musicas" ou "artistas" passando como parâmetro uma descrição, um ID  e um Token de acesso. Caso encontrado os recursos poderão ser inseridos na base de dados do app-music, para futuramente serem adicionados à uma playlist.

A documentação do microsserviço foi documentada pelo Swagger. Visualização através da seguinte URL:

- http://[host]/swagger-ui.html
