import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-updateinfo',
  templateUrl: './updateinfo.component.html',
  styleUrls: ['./updateinfo.component.scss']
})
export class UpdateinfoComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit(): void {
  }

  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }

}
