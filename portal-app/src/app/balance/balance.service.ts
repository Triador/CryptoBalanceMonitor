import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Balance } from '../models/balance.model';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class BalanceService {

  constructor(private http:HttpClient) {}

  private balanceUrl = 'http://localhost:8080/api';

  public getBalances() {
    return this.http.get<Balance[]>(this.balanceUrl);
  }
}
