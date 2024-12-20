import { Component, OnInit } from '@angular/core';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';
import { fromLonLat, toLonLat } from 'ol/proj';
import { Tile as TileLayer } from 'ol/layer';
import { OSM } from 'ol/source';
import { Map } from 'ol';
import { View } from 'ol';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import { Coordinate } from 'ol/coordinate';
import { Style, Icon, Circle as CircleStyle, Fill, Stroke } from 'ol/style';
import { HttpClient } from '@angular/common/http';
import { Post } from '../model/post.model';
import { PostService } from '../post.service';
import { LikeService } from '../like.service';
import { PagedResults } from '../model/paged-results.model';
import { Like } from '../model/like.model';

@Component({
  selector: 'app-map-post',
  templateUrl: './map-post.component.html',
  styleUrls: ['./map-post.component.css']
})
export class MapPostComponent implements OnInit {

  registerUser: RegisterUser | undefined;
  posts: Post[]=[];
  private map: Map | undefined;
  private vectorSource = new VectorSource();

  constructor(private userService: LoginService, private http: HttpClient, private postService: PostService,private likeService: LikeService){}

  ngOnInit(): void {
    this.userService.user$.subscribe({
      next: (user) =>{
        if(user&& user.id){
          this.loadUserData(user.id);
        }
      },
      error: (error)=>{
        console.log("Greska prilikom dobijanja korisnika", error);
        alert("Greska pri ucitavanju profila");
      }
    });

    this.loadPosts();
  }

  loadUserData(userId: number): void {
    this.userService.getUserForProfile(userId).subscribe({
      next: (user) => {
        this.registerUser = user;
        if (this.registerUser && this.registerUser.adress) {
          let coordinates: { lat: number; lon: number };
          this.getCoordinates(this.registerUser.adress)
            .then((result) => {
              coordinates = result; // Sačuvaj koordinate
              return this.initializeMap(coordinates.lat, coordinates.lon);
            })
            .then(() => {
              // Dodaj korisnički marker nakon inicijalizacije mape
              this.addUserMarker(coordinates.lat, coordinates.lon);
            })
            .catch((error) => {
              console.error('Greška prilikom geokodiranja adrese', error);
              alert('Nije moguće prikazati mapu za zadatu adresu.');
            });
        }
      },
      error: (error) => {
        console.error('Greška prilikom učitavanja profila', error);
      },
    });
  }

  getCoordinates(address: string): Promise<{ lat: number; lon: number }> {
    const url = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(
      address
    )}&format=json&limit=1`;

    return this.http.get<any[]>(url).toPromise().then((result) => {
      if(result){
      if (result.length > 0) {
        return { lat: parseFloat(result[0].lat), lon: parseFloat(result[0].lon) };
      } else {
        throw new Error('Adresa nije pronađena');
      }
    }
    else
    throw new Error("result je undefined");
    });
  }

  initializeMap(lat: number, lon: number): void {
    
    const vectorLayer = new VectorLayer({
      source: this.vectorSource,
    });
  
    this.map = new Map({
      target: 'map',
      layers: [
        new TileLayer({
          source: new OSM(),
        }),
        vectorLayer,
      ],
      view: new View({
        center: fromLonLat([lon, lat]), // Centriranje na koordinate adrese
        zoom: 14,
      }),
    });
  
    // Dodaj marker za korisnika
    this.addUserMarker(lat, lon);
  
    // Dodaj markere za postove nakon inicijalizacije mape
    if (this.posts.length > 0) {
      this.addPostMarkers();
    } else {
      console.warn('Nema dostupnih postova za dodavanje markera.');
    }
  }
  
    // Dodavanje pokazivača
    addUserMarker(lat:number,lon:number): void{
      const userMarker= new Feature({
        geometry: new Point(fromLonLat([lon,lat])),
      });

      userMarker.setStyle(
        new Style({
          image: new Icon({
            src: 'https://cdn-icons-png.flaticon.com/512/684/684908.png', // Ikonica korisnika
          scale: 0.05,
          }),
        })
      );

      this.vectorSource.addFeature(userMarker);
    }

    addPostMarkers(): void {
    this.posts.forEach((post) => {
      if (post.location && post.location.latitude !== undefined && post.location.longitude !== undefined) {
        
        console.log(`Dodavanje markera za post ID ${post.id} na lokaciju: (${post.location.latitude}, ${post.location.longitude})`);
        
        const postMarker = new Feature({
          geometry: new Point(fromLonLat([post.location.longitude, post.location.latitude])
        ),
        });

        postMarker.setStyle(
          new Style({
            image: new Icon({
              src: 'https://cdn-icons-png.flaticon.com/512/684/684908.png', // Ikonica za post
              scale: 0.05,
            }),
          })
        );

        this.vectorSource.addFeature(postMarker);
      }
      else{
        console.warn(`Post ID ${post.id} nema validne koordinate`);
      }
    });
  }


  loadPosts(): void {
        this.postService.getAllPosts().subscribe(
          (data: PagedResults<Post>) => {
            this.posts = data.results.filter((p)=>p.creatorId !== this.registerUser?.id); // Uzimamo postove iz rezultata
           if(this.map){
            this.addPostMarkers();
           }
           else
           {
            console.warn("Mapa nije inicijalizovana pre ubacivanja markera");
           
            const mapInitializationCheck = setInterval(() => {
              if (this.map) {
                clearInterval(mapInitializationCheck);
                this.addPostMarkers();
              }
            }, 100); // Proveravaj svakih 100ms
          
          }
           
            this.posts.forEach(post => {
              this.loadLikes(post);
            });
            
          },
          (error) => {
            console.error('Greška pri učitavanju postova', error); // Obrada greške
          }
        );
      }
    
      loadLikes(post: Post): void{
        this.likeService.getLikesByPostId(post.id).subscribe((pagedResults: PagedResults<Like>) => {
              post.likes = pagedResults.results;
              post.likeCount = pagedResults.results.length;
              post.userHasLiked = post.likes.some((like:Like)=> like.username === this.registerUser?.username);
            });
      }
}
