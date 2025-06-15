import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';

export interface CustomEffectField {
  key: string;
  description: string;
  type: string | null;
  minimum: number | string | null;
  maximum: number | string | null;
}

export interface CustomEffect {
  effectType: string;
  fields: CustomEffectField[];
}

@Injectable({
  providedIn: 'root'
})
export class CustomEffectsService {
  constructor(private http: HttpClient) {}

  private customEffectSubject = new BehaviorSubject<{ [key: string]: CustomEffect }>({});
  customEffects$ = this.customEffectSubject.asObservable();

  fetchOptions(): void {
    this.http.get<{ [key: string]: CustomEffect }>('assets/data/custom-effects.json')
      .subscribe(data => {
        this.customEffectSubject.next(data);
      });
  }
}
