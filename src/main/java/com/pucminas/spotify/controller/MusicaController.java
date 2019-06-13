package com.pucminas.spotify.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="Consulta músicas")
@RestController
@RequestMapping("/")
public class MusicaController {
	private String accessToken = "BQC9XhiwTjrNue-OcZKSzZxZZLJFNNPfcq6eXErIYCX-V0-A4PcUOCmwmoM0WpbXqecrGgVelea_zZEJ1UwIibS64EHguX5qwK-WkpAaA9TPVtPyxBW30R7tI-QKPFJv-Pftyc68hM9yY6CBWwHe";
    private SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
    private static final CacheControl cacheControl = CacheControl.maxAge(20, TimeUnit.SECONDS);		
    
    private void refreshSpotify(String token) {
    	this.accessToken = token;
    	this.spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
    }
    
	@HystrixCommand(fallbackMethod = "getDefaultMusica")	
	@ApiOperation(value = "Recupera musicas passando token autenticado do Spotify.", response = List.class)
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Músicas recuperadas com sucesso!"),
	    @ApiResponse(code = 401, message = "Você não está autorizado a visualizar o recurso."),
	    @ApiResponse(code = 403, message = "Proibido acessar esse recurso."),
	    @ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado.") }
	)    
    @GetMapping (value = "/musicas")
    @ResponseBody
    public ResponseEntity<?> getMusicas(@RequestParam String token) throws SpotifyWebApiException, IOException{
        String q = "Raul";
        token = "BQC9XhiwTjrNue-OcZKSzZxZZLJFNNPfcq6eXErIYCX-V0-A4PcUOCmwmoM0WpbXqecrGgVelea_zZEJ1UwIibS64EHguX5qwK-WkpAaA9TPVtPyxBW30R7tI-QKPFJv-Pftyc68hM9yY6CBWwHe";
    	refreshSpotify(token);
    	SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(q).build();
    	return ResponseEntity.status(HttpStatus.OK).cacheControl(cacheControl).body(searchTracksRequest.getJson());
    }
	
	public String getDefaultMusica() {
	    return "Consulta indisponível, tente novamente mais tarde!!!";
	}	
	    
	
}
