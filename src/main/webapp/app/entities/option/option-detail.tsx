import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './option.reducer';

export const OptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const optionEntity = useAppSelector(state => state.option.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="optionDetailsHeading">
          <Translate contentKey="mcqApp.option.detail.title">Option</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{optionEntity.id}</dd>
          <dt>
            <span id="option">
              <Translate contentKey="mcqApp.option.option">Option</Translate>
            </span>
          </dt>
          <dd>{optionEntity.option}</dd>
          <dt>
            <span id="isCorrect">
              <Translate contentKey="mcqApp.option.isCorrect">Is Correct</Translate>
            </span>
          </dt>
          <dd>{optionEntity.isCorrect ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="mcqApp.option.question">Question</Translate>
          </dt>
          <dd>{optionEntity.question ? optionEntity.question.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/option" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/option/${optionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OptionDetail;
