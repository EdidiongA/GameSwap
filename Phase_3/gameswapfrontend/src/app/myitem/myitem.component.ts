import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-myitem',
  templateUrl: './myitem.component.html',
  styleUrls: ['./myitem.component.scss']
})

export class MyitemComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit(): void {
  }

  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }
}
