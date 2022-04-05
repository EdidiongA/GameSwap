import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GameSwapItem } from './models';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GameswapService {
  // chrome.exe --user-data-dir="C://Chrome dev session" --disable-web-security
  rootURL = 'http://localhost:8081/';

  
  private selectedItem: BehaviorSubject<GameSwapItem> = new BehaviorSubject<GameSwapItem>(null);
  currentItem = this.selectedItem.asObservable();

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
