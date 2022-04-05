import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-swaphistory',
  templateUrl: './swaphistory.component.html',
  styleUrls: ['./swaphistory.component.scss']
})
export class SwaphistoryComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit(): void {
  }
  
  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }

  goToSwapDetails() {
    this.router.navigate(['/SwapDetails']);
  }
}
