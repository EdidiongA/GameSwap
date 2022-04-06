import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GameSwapItem } from './models';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GameswapService {
  // chrome.exe --user-data-dir="C://Chrome dev session" --disable-web-security
  rootURL = 'http://localhost:8081/GameSwap/';

  
  private selectedItem: BehaviorSubject<GameSwapItem> = new BehaviorSubject<GameSwapItem>(null);
  currentItem = this.selectedItem.asObservable();

  private userId: BehaviorSubject<string> = new BehaviorSubject<string>(null);
  currentUser = this.userId.asObservable();

  currentUserId
  updateUserId(userId: string) {
    this.userId.next(userId)     
    this.currentUserId = userId
  }

  updateSelectedItem(item: GameSwapItem) {
    this.selectedItem.next(item)     
  }

  constructor(private http: HttpClient) { }  

  addUser() {
    console.log('Add user');
    this.http.post<any>(this.rootURL + 'user', {}).subscribe({
        next: data => {
       
        },
        error: error => {          
            console.error('There was an error!', error);
        }
    })
  }

  getUserInfo(userId) {
    console.log('get user');
    return this.http.get<any>(this.rootURL + 'user', {
      params: {
        email: userId
           }
    }).toPromise()
  }

  getItemForSwap(userId) {
    return this.http.get<any[]>(this.rootURL + 'item/owned', {
      headers: {
        email: this.currentUserId
           }
    }).toPromise()
  }

  getAuthentication(loginId, password) {
    console.log('get user');
    return this.http.get<any>(this.rootURL + 'user/authenticate', {
      params: {
        email: loginId,
        password: password
           }
    }).toPromise()

    // this.http.get(this.rootURL + 'user', {
    //   params: {
    //     email: loginId
    //   },
    //   observe: 'response'
    // })
    // .toPromise()
    // .then(response => {
    //   console.log(response);
    // })
    // .catch(console.log);
  }

  getPostalCodes() {
    const postalCodes = [
      '00000',
      '11111',
      '22222',
      '33333'
    ];

    // TODO update with call to retrive postal codes from the server
    return new Promise(function(resolve) {
      setTimeout(() => resolve(postalCodes), 3000);
    });
  }
}
