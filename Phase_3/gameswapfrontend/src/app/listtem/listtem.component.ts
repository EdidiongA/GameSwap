import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl } from '@angular/forms'; 


interface Type {
  value: string;
  viewValue: string;
} 

@Component({
  selector: 'app-listtem',
  templateUrl: './listtem.component.html',
  styleUrls: ['./listtem.component.scss']
})

export class ListtemComponent implements OnInit {

  constructor(private router:Router) { }

  gametype
  platform
  platformOS
  media
  title
  pieceCount
  condition
  description


  ngOnInit(): void {
  }

  onGameTypeSelect() {
    //called wnen a a game type is selected. 
        
  } 

  listItem() {

  }

  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }
}
