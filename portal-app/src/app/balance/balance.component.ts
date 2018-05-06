import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Balance } from '../models/balance.model';
import { BalanceService } from './balance.service';

@Component({
  selector: 'app-balance',
  templateUrl: './balance.component.html',
  styleUrls: ['./balance.component.css']
})
export class BalanceComponent implements OnInit {

  balances: Balance[];

  constructor(private router: Router, private balanceService: BalanceService) {

  }

  ngOnInit() {
    this.balanceService.getBalances()
      .subscribe( data => {
        this.balances = data;
      })
  };
}