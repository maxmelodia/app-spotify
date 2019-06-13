package com.pucminas.spotify.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.search.SearchItemRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="Consulta Artistas")
@RestController
@RequestMapping("/")
public class ArtistaController {
	private String accessToken = "BQADC5pLX-XOANQ2LDbh4ZQVUEz-yxXpxElZ_CxuRGa53255Yufj7sNEAV6v3bR3Fga_DIJD4bTaF4-YESm8rn1Jcp-gJ1L_yFShJ-qQo2MbOlzoXMVSnNj7CPt1D_N5B3ZeeWGPuPjtGBFri1xd";
    private SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
    private static final CacheControl cacheControl = CacheControl.maxAge(20, TimeUnit.SECONDS);		
    
    private void refreshSpotify(String token) {
    	this.accessToken = token;
    	this.spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
    }
    
	@HystrixCommand(fallbackMethod = "getDefaultArtista")	
	@ApiOperation(value = "Recupera artistas passando token autenticado do Spotify.", response = List.class)
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Artistas recuperados com sucesso!"),
	    @ApiResponse(code = 401, message = "Você não está autorizado a visualizar o recurso."),
	    @ApiResponse(code = 403, message = "Proibido acessar esse recurso."),
	    @ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado.") }
	)    
    @GetMapping (value = "/artistas")
    @ResponseBody
    public ResponseEntity<?> getArtistas(@RequestParam String token) throws SpotifyWebApiException, IOException{
        String q = "Raul";
        String type = ModelObjectType.ARTIST.getType();
    	refreshSpotify(token);
    	
    	
    	SearchItemRequest searchItemRequest = spotifyApi.searchItem(q, type).limit(10).offset(0).build();
    	
    	return ResponseEntity.status(HttpStatus.OK).cacheControl(cacheControl).body(searchItemRequest.getJson());
    }
    
	@ApiOperation(value = "Recupera artista passando token autenticado do Spotify e o ID do artista.", response = List.class)
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Artistas recuperados com sucesso!"),
	    @ApiResponse(code = 401, message = "Você não está autorizado a visualizar o recurso."),
	    @ApiResponse(code = 403, message = "Proibido acessar esse recurso."),
	    @ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado.") }
	) 	
	@GetMapping("/artistas/{id}")
	public ResponseEntity<?> findOne(@PathVariable("id") String id, @RequestParam String token){
		refreshSpotify(token);
        Artist a = null;
		try {
		      GetArtistRequest getArtistRequest = spotifyApi.getArtist("5eAWCfyUhZtHHtBdNk56l1").build();
   			  a = getArtistRequest.execute();
		} catch (SpotifyWebApiException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).cacheControl(cacheControl).body(a);
	}
	
	public String getDefaultArtista() {
	    return "Consulta indisponível, tente novamente mais tarde!!!";
	}	
	
}
